package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.*
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.models.extensions.transformToAverageList
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_CYCLES
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_DISCIPLINE_ID
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_EXAM
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity.Companion.EXTRA_IS_ON_EDIT_MODE
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyExamsAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.ExamsByCyclesAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.LoadingAdapter
import com.doubleb.meusemestre.ui.dialogs.DialogConfirmRemoval
import com.doubleb.meusemestre.ui.listeners.ExamListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.DisciplinesViewModel
import com.doubleb.meusemestre.viewmodel.ExamsViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discipline_details.*
import org.koin.android.ext.android.inject
import kotlin.math.abs

class DisciplineDetailsFragment : Fragment(R.layout.fragment_discipline_details),
    EmptyStateView.ClickListener, ExamListener {

    companion object {
        private const val DISCIPLINE_ITEM_POSITION = "DISCIPLINE_ITEM_POSITION"
        private const val DISCIPLINE_ITEM = "DISCIPLINE_ITEM"

        const val REQUEST_SUCCESS = "REQUEST_SUCCESS"

        fun instance(discipline: Discipline?, itemPosition: Int) =
            DisciplineDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DISCIPLINE_ITEM, discipline)
                    putInt(DISCIPLINE_ITEM_POSITION, itemPosition)
                }
            }
    }

    //region immutable vars

    //region adapters
    private val examsAdapter by lazy { ExamsByCyclesAdapter(listener = this) }
    private val emptyExamsAdapter by lazy { EmptyExamsAdapter(this) }
    private val loadingAdapter by lazy { LoadingAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(loadingAdapter) }
    //endregion

    private val clickListener by lazy {
        View.OnClickListener { launchExamRegistrationActivity() }
    }

    //region viewmodels
    private val disciplinesViewModel: DisciplinesViewModel by inject()
    private val examsViewModel: ExamsViewModel by inject()
    //endregion

    //region components
    private val homeActivity by lazy { (activity as? HomeActivity) }
    private val dialogConfirmRemoval by lazy { DialogConfirmRemoval().type(DialogConfirmRemoval.Type.DISCIPLINE) }
    //endregion

    //endregion

    //region mutable vars
    private var itemPosition: Int = 0
    private var discipline: Discipline? = null
    private var hasChanged = false

    private lateinit var examRegistrationCallback: ActivityResultLauncher<Intent>
    //endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discipline = arguments?.getParcelable(DISCIPLINE_ITEM)
        itemPosition = arguments?.getInt(DISCIPLINE_ITEM_POSITION) ?: 0

        examRegistrationCallback = createActivityResultLauncher {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                hasChanged = true
                examsViewModel.getExams(discipline?.id)
            }
        }

        examsViewModel.liveData.observe(viewLifecycleOwner, observeExams())
        disciplinesViewModel.liveDataDisciplineRemoval.observe(viewLifecycleOwner,
            observeDisciplineRemoval())

        configureViews()
        examsViewModel.getExams(discipline?.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeActivity?.showNavigation()
        dialogConfirmRemoval.dismiss()
    }

    override fun onResume() {
        super.onResume()
        homeActivity?.configureActionButton(
            R.string.discipline_detail_fab,
            !examsAdapter.list.isNullOrEmpty(),
            clickListener
        )
    }

    override fun onPause() {
        super.onPause()
        homeActivity?.clearActionButton()
    }

    //region listeners
    override fun onEmptyViewActionClick(view: View) {
        launchExamRegistrationActivity()
    }

    override fun onExamClick(parentPosition: Int, position: Int) {
        val examList = examsAdapter.list?.getOrNull(parentPosition)?.second
        val exam = examList?.getOrNull(position)
        launchExamRegistrationActivity(exam)
    }
    //endregion

    private fun configureViews() {
        context?.let {
            discipline_details_image_view_wave.background = it.swipeWaveByPosition(itemPosition)

            discipline_details_toolbar.setBackgroundColor(it.swipeColorByPosition(itemPosition))
            discipline_detail_constraint.setBackgroundColor(it.swipeColorByPosition(itemPosition))

            discipline_details_image_view_knowledge_area.loadImage(discipline?.image)
        }

        discipline_toolbar_image_arrow.setOnClickListener {
            onBackPressed()
        }

        discipline_toolbar_image_delete.setOnClickListener {
            dialogConfirmRemoval
                .title(discipline?.name)
                .listener {
                    disciplinesViewModel.removeDiscipline(discipline?.id.orEmpty())
                }
                .show(childFragmentManager)
        }

        discipline_details_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
            discipline_toolbar_text_title.text = discipline?.name.takeIf { isCollapsed }.orEmpty()
        })

        homeActivity?.hideNavigation()
        discipline_details_recyclerview.adapter = concatAdapter
        discipline_details_average_indicator.title(discipline?.name).average(discipline?.average)
    }

    //region observers
    private fun observeDisciplineRemoval() = Observer<DataSource<List<Discipline>>> {
        if (it.dataState == DataState.SUCCESS) {
            hasChanged = true
        }

        onBackPressed()
    }

    private fun observeExams() = Observer<DataSource<List<Pair<Int?, List<Exam>>>>> {
        when (it.dataState) {
            DataState.LOADING -> {
                buildFullLoadingState()
            }

            DataState.SUCCESS -> {
                buildDisciplineDetails(it.data)
            }

            DataState.ERROR -> {

            }
        }
    }
    //endregion

    private fun buildEmptyState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(loadingAdapter)
        concatAdapter.removeAdapter(examsAdapter)

        concatAdapter.addAdapter(emptyExamsAdapter)
    }

    private fun buildFullLoadingState() {
        homeActivity?.hideFab()

        concatAdapter.removeAdapter(emptyExamsAdapter)
        concatAdapter.removeAdapter(examsAdapter)

        concatAdapter.addAdapter(loadingAdapter)
    }

    private fun addExams(list: List<Pair<Int?, List<Exam>>>) {
        homeActivity?.showFab()

        concatAdapter.removeAdapter(emptyExamsAdapter)
        concatAdapter.removeAdapter(loadingAdapter)

        examsAdapter.list = list
        concatAdapter.addAdapter(examsAdapter)
    }

    private fun buildDisciplineDetails(list: List<Pair<Int?, List<Exam>>>?) {
        buildAverageIndicator(list)

        if (list.isNullOrEmpty()) {
            buildEmptyState()
        } else {
            addExams(list)
        }
    }

    private fun buildAverageIndicator(list: List<Pair<Int?, List<Exam>>>?) {
        val gradeResultList = list?.transformToAverageList()

        val average =
            if (gradeResultList.isNullOrEmpty()) null else gradeResultList.average().toFloat()
        discipline_details_average_indicator.average(average)
    }

    private fun launchExamRegistrationActivity(exam: Exam? = null) = discipline?.id?.let {
        examRegistrationCallback.launchActivity<ExamRegistrationActivity>(
            context,
            EXTRA_DISCIPLINE_ID to it,
            EXTRA_CYCLES to homeActivity?.user?.graduation_info?.cycles,
            EXTRA_IS_ON_EDIT_MODE to (exam != null),
            EXTRA_EXAM to exam
        )
    }

    private fun onBackPressed() {
        homeActivity?.onBackPressed()
        if (hasChanged) {
            setFragmentResult(REQUEST_SUCCESS, bundleOf())
        }
    }
}