package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.createActivityResultLauncher
import com.doubleb.meusemestre.extensions.isValid
import com.doubleb.meusemestre.extensions.launchActivity
import com.doubleb.meusemestre.models.ActiveSemester
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity.Companion.CURRENT_SEMESTER_EXTRA
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ActiveSemesterAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyDisciplinesAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptySemesterAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.LoadingAdapter
import com.doubleb.meusemestre.ui.dialogs.BottomSheetSemesterRegistration
import com.doubleb.meusemestre.ui.dialogs.DialogConfirmRemoval
import com.doubleb.meusemestre.ui.fragments.DisciplineDetailsFragment.Companion.REQUEST_SUCCESS
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.*
import kotlinx.android.synthetic.main.fragment_active_semester.*
import org.koin.android.ext.android.inject

class ActiveSemesterFragment : Fragment(R.layout.fragment_active_semester), DisciplineListener,
    EmptyStateView.ClickListener,
    BottomSheetSemesterRegistration.SemesterRegistrationClickListener {

    companion object {
        private const val INSTANCE_STATE_CLICKED_POSITION = "INSTANCE_STATE_CLICKED_POSITION"
    }

    //region immutable vars

    //region adapters
    private val activeSemesterAdapter by lazy { ActiveSemesterAdapter(this) }
    private val emptyDisciplinesAdapter by lazy { EmptyDisciplinesAdapter(this) }
    private val emptySemesterAdapter by lazy { EmptySemesterAdapter(getEmptySemesterListener()) }
    private val loadingAdapter by lazy { LoadingAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(loadingAdapter) }
    //endregion

    //region components
    private val homeActivity by lazy { (activity as? HomeActivity) }
    private val bottomSheet by lazy { BottomSheetSemesterRegistration(this) }
    private val dialogConfirmRemoval by lazy { DialogConfirmRemoval().type(DialogConfirmRemoval.Type.DISCIPLINE) }
    //endregion

    //region listeners
    private val clickListener by lazy { View.OnClickListener { launchDisciplineRegistrationActivity() } }
    //endregion

    //region viewmodels
    private val activeSemesterViewModel: ActiveSemesterViewModel by inject()
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    private val semesterViewModel: SemesterViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var user: User? = null
    private var clickedPosition: Int = 0

    private lateinit var disciplineRegistrationCallback: ActivityResultLauncher<Intent>
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            clickedPosition = it.getInt(INSTANCE_STATE_CLICKED_POSITION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disciplineRegistrationCallback = createActivityResultLauncher {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                disciplinesViewModel.getDisciplines()
            }
        }

        parentFragment?.setFragmentResultListener(REQUEST_SUCCESS) { _, _ ->
            activeSemesterViewModel.getActiveSemester()
        }

        semesterViewModel.livedata
            .observe(viewLifecycleOwner, observeSemesterCreation())
        activeSemesterViewModel.liveData
            .observe(viewLifecycleOwner, observeActiveSemester())
        disciplinesViewModel.liveDataDiscipline
            .observe(viewLifecycleOwner, observeDisciplinesRecovery())

        active_semester_recycler_view.adapter = concatAdapter

        if (activeSemesterAdapter.currentList.isNullOrEmpty()) {
            activeSemesterViewModel.getActiveSemester()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(INSTANCE_STATE_CLICKED_POSITION, clickedPosition)
    }

    override fun onResume() {
        super.onResume()
        homeActivity?.configureActionButton(
            R.string.active_semester_fab,
            activeSemesterAdapter.currentList.isNotEmpty(),
            clickListener
        )
    }

    override fun onPause() {
        super.onPause()
        homeActivity?.clearActionButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogConfirmRemoval.dismiss()
        bottomSheet.dismiss()
    }
    //endregion

    //region listeners
    override fun onDisciplineClick(position: Int) {
        clickedPosition = position

        homeActivity?.inflateStackFragment(
            DisciplineDetailsFragment.instance(
                activeSemesterAdapter.currentList[position],
                position
            )
        )
    }

    override fun onDisciplineDelete(position: Int) {
        homeActivity?.expand()

        activeSemesterAdapter.currentList[position].name?.let { disciplineName ->
            dialogConfirmRemoval
                .title(disciplineName)
                .listener {
                    val targetDiscipline = activeSemesterAdapter.currentList[position]
                    val disciplines =
                        ArrayList(activeSemesterAdapter.currentList).apply { removeAt(position) }

                    activeSemesterAdapter.submitList(disciplines)
                    disciplinesViewModel.removeDiscipline(targetDiscipline.id.orEmpty())

                    if (disciplines.isEmpty()) {
                        buildEmptyDisciplinesState()
                    }
                }
                .show(childFragmentManager)
        }

    }

    override fun onEmptyViewActionClick(view: View) {
        launchDisciplineRegistrationActivity()
    }

    override fun onCreateSemester(name: String) {
        semesterViewModel.createSemester(name)
    }

    private fun getEmptySemesterListener() = EmptyStateView.ClickListener {
        bottomSheet.show(childFragmentManager, BottomSheetSemesterRegistration.TAG)
    }
    //endregion

    //region observers
    private fun observeActiveSemester() = Observer<DataSource<ActiveSemester>> {
        when (it.dataState) {
            DataState.LOADING -> {
            }

            DataState.SUCCESS -> {
                this.user = it.data?.user
                buildActiveSemester(it.data?.disciplines)
            }

            DataState.ERROR -> {
            }
        }
    }

    private fun observeDisciplinesRecovery() = Observer<DataSource<List<Discipline>>> {
        when (it.dataState) {
            DataState.LOADING -> {
                buildFullLoadingState()
            }

            DataState.SUCCESS -> {
                addDisciplines(it.data)
            }

            DataState.ERROR -> {
            }
        }
    }

    private fun observeSemesterCreation() = Observer<DataSource<String>> {
        when (it.dataState) {
            DataState.LOADING -> {

            }

            DataState.SUCCESS -> {
                homeActivity?.user = homeActivity?.user?.copy(current_semester = it.data)
                bottomSheet.dismiss()
                buildEmptyDisciplinesState()
            }

            DataState.ERROR -> {
                bottomSheet.dismiss()
            }
        }
    }
    //endregion

    private fun buildFullLoadingState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(emptyDisciplinesAdapter)
        concatAdapter.removeAdapter(activeSemesterAdapter)

        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun buildEmptyDisciplinesState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(activeSemesterAdapter)

        if (homeActivity?.user?.current_semester.isValid()) {
            concatAdapter.removeAdapter(emptySemesterAdapter)
            concatAdapter.addAdapter(emptyDisciplinesAdapter)
        } else {
            concatAdapter.removeAdapter(emptyDisciplinesAdapter)
            concatAdapter.addAdapter(emptySemesterAdapter)
        }
    }

    private fun buildActiveSemester(disciplines: List<Discipline>?) {
        if (!disciplines.isNullOrEmpty()) {
            addDisciplines(disciplines)
        } else {
            buildEmptyDisciplinesState()
        }
    }

    private fun addDisciplines(list: List<Discipline>?) {
        homeActivity?.showFab()

        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)

        activeSemesterAdapter.submitList(list)
        concatAdapter.addAdapter(activeSemesterAdapter)
    }

    private fun launchDisciplineRegistrationActivity() {
        disciplineRegistrationCallback.launchActivity<DisciplineRegistrationActivity>(
            context, CURRENT_SEMESTER_EXTRA to user?.current_semester
        )
    }
}