package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.models.ActiveSemester
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ActiveSemesterAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyDisciplinesAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.LoadingAdapter
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.ActiveSemesterViewModel
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.DisciplinesViewModel
import kotlinx.android.synthetic.main.fragment_active_semester.*
import org.koin.android.ext.android.inject

class ActiveSemesterFragment : Fragment(R.layout.fragment_active_semester), DisciplineListener,
    EmptyStateView.ClickListener {

    //region immutable vars

    //region adapters
    private val activeSemesterAdapter by lazy { ActiveSemesterAdapter(this) }
    private val emptyDisciplinesAdapter by lazy { EmptyDisciplinesAdapter(this) }
    private val loadingAdapter by lazy { LoadingAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(loadingAdapter) }
    //endregion

    //region components
    private val fab by lazy { (activity as? HomeActivity)?.fab() }
    //endregion

    //region listeners
    private val clickListener by lazy {
        View.OnClickListener {
            disciplineRegistrationCallback.launch(
                Intent(context, DisciplineRegistrationActivity::class.java)
                    .putExtra(DisciplineRegistrationActivity.CURRENT_SEMESTER_EXTRA,
                        user?.current_semester)
            )
        }
    }
    //endregion

    //region viewmodels
    private val activeSemesterViewModel: ActiveSemesterViewModel by inject()
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    //endregion

    //endregion

    //region mutable vars
    private var user: User? = null

    private lateinit var disciplineRegistrationCallback: ActivityResultLauncher<Intent>
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disciplineRegistrationCallback = DisciplineRegistrationActivity.newInstanceForResult(this) {
            disciplinesViewModel.getDisciplines()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activeSemesterViewModel.liveData.observe(viewLifecycleOwner, observeActiveSemester())
        disciplinesViewModel.liveDataDiscipline.observe(viewLifecycleOwner,
            observeDisciplinesRecovery())

        active_semester_recycler_view.adapter = concatAdapter

        activeSemesterViewModel.getActiveSemester()
    }

    override fun onResume() {
        super.onResume()
        fab?.let {
            it.setOnClickListener(clickListener)
            it.setText(R.string.active_semester_fab)

            if (activeSemesterAdapter.currentList.isNotEmpty()) {
                showFab()
            } else {
                it.gone()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fab?.let {
            it.gone()
            it.setOnClickListener(null)
        }
    }
    //endregion

    //region listeners
    override fun onDisciplineClick(position: Int) {
        (activity as? HomeActivity)?.inflateStackFragment(
            DisciplineDetailsFragment.instance(
                activeSemesterAdapter.currentList[position].name,
                activeSemesterAdapter.currentList[position].grade
            )
        )
    }

    override fun onDisciplineDelete(position: Int) {
        (activity as? HomeActivity)?.expand()
        val targetDiscipline = activeSemesterAdapter.currentList[position]
        val disciplines = ArrayList(activeSemesterAdapter.currentList).apply { removeAt(position) }

        activeSemesterAdapter.submitList(disciplines)
        disciplinesViewModel.removeDiscipline(targetDiscipline.id.orEmpty())

        if (disciplines.isEmpty()) {
            buildEmptySemesterState()
        }
    }

    override fun onEmptyViewActionClick(view: View) {
        disciplineRegistrationCallback.launch(
            Intent(context, DisciplineRegistrationActivity::class.java)
                .putExtra(DisciplineRegistrationActivity.CURRENT_SEMESTER_EXTRA,
                    user?.current_semester)
        )
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
    //endregion

    private fun buildFullLoadingState() {
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)
        concatAdapter.removeAdapter(activeSemesterAdapter)

        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun buildEmptySemesterState() {
        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(activeSemesterAdapter)

        concatAdapter.addAdapter(emptyDisciplinesAdapter)
    }

    private fun buildActiveSemester(disciplines: MutableList<Discipline>?) {
        if (!disciplines.isNullOrEmpty()) {
            showFab()
            addDisciplines(disciplines)
        } else {
            buildEmptySemesterState()
        }
    }

    private fun addDisciplines(list: List<Discipline>?) {
        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(emptyDisciplinesAdapter)

        activeSemesterAdapter.submitList(list)
        concatAdapter.addAdapter(activeSemesterAdapter)
    }

    private fun showFab() = fab?.run {
        show()
        extend()
    }
}