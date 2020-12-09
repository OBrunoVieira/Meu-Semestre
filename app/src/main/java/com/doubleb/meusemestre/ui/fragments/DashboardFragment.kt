package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.*
import com.doubleb.meusemestre.ui.dialogs.BottomSheetSemesterRegistration
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import com.doubleb.meusemestre.viewmodel.SemesterViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject

class DashboardFragment : Fragment(R.layout.fragment_dashboard),
    DisciplineListener,
    EmptyStateView.ClickListener,
    BottomSheetSemesterRegistration.SemesterRegistrationClickListener {

    //region adapters
    private val disciplineListAdapter by lazy { DisciplineListAdapter() }
    private val bestDisciplineAdapter by lazy { BestDisciplineAdapter() }
    private val restrictedDisciplineListAdapter by lazy { RestrictedDisciplineListAdapter() }
    private val registerGradesAdapter by lazy { RegisterGradesAdapter() }
    private val finishSemesterAdapter by lazy { FinishSemesterAdapter() }
    private val gradeHighlightAdapter by lazy { GradeHighlightAdapter() }
    private val emptySemesterAdapter by lazy { EmptySemesterAdapter(this) }
    private val concatAdapter by lazy { ConcatAdapter(disciplineListAdapter) }
    //endregion

    private val bottomSheet by lazy { BottomSheetSemesterRegistration(this) }
    private val semesterViewModel: SemesterViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disciplineListAdapter.list = listOf(
            Discipline("", "Fotografia", "" ,10f),
            Discipline("", "Sociologia", "" ,2f),
            Discipline("", "Desenvolvimento de Banco de Dados II", "" ,2f),
            Discipline("", "Química", "" ,5f),
            Discipline("", "Álgebra Linear", "" ,3.4f),
            Discipline("", "Cálculo I", "" ,7f),
            Discipline("", "Ilustração", "" ,9f),
            Discipline("", "Literatura Moderna", "" ,9.5f),
            Discipline("", "Literatura Clássica", "" ,8f)
        )

        dashboard_recycler_view.adapter = concatAdapter
        disciplineListAdapter.listener = this
    }

    override fun onDisciplineClick(position: Int) {
        (activity as? HomeActivity)?.inflateStackFragment(
            DisciplineDetailsFragment.instance(
                disciplineListAdapter.list?.get(position)?.name,
                disciplineListAdapter.list?.get(position)?.grade
            )
        )
    }

    //region Semester Creation
    override fun onCreateSemester(name: String) {
        semesterViewModel.createSemester(name)
    }

    override fun onEmptyViewActionClick(view:View) {
        bottomSheet.show(childFragmentManager, BottomSheetSemesterRegistration.TAG)
    }
    //endregion
}