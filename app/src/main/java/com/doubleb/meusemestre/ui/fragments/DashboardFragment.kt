package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private val disciplineListAdapter by lazy { DisciplineListAdapter() }
    private val bestDisciplineAdapter by lazy { BestDisciplineAdapter() }
    private val restrictedDisciplineListAdapter by lazy { RestrictedDisciplineListAdapter() }
    private val registerGradesAdapter by lazy { RegisterGradesAdapter() }
    private val finishSemesterAdapter by lazy { FinishSemesterAdapter() }
    private val gradeHighlightAdapter by lazy { GradeHighlightAdapter() }

    private val concatAdapter by lazy {
        ConcatAdapter(
            disciplineListAdapter,
            bestDisciplineAdapter,
            restrictedDisciplineListAdapter,
            gradeHighlightAdapter,
            registerGradesAdapter,
            finishSemesterAdapter
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboard_recycler_view.adapter = concatAdapter
        bestDisciplineAdapter.discipline = Discipline("", "Fotografia", 10f)

        disciplineListAdapter.list = listOf(
            Discipline("", "Fotografia", 10f),
            Discipline("", "Sociologia", 2f),
            Discipline("", "Desenvolvimento de Banco de Dados II", 2f),
            Discipline("", "Química", 5f),
            Discipline("", "Álgebra Linear", 3.4f),
            Discipline("", "Cálculo I", 7f),
            Discipline("", "Ilustração", 9f),
            Discipline("", "Literatura Moderna", 9.5f),
            Discipline("", "Literatura Clássica", 8f)
        )

        restrictedDisciplineListAdapter.list = listOf(
            Discipline("", "Álgebra Linear", 3.4f),
            Discipline("", "Sociologia", 2f),
            Discipline("", "Desenvolvimento de Banco de Dados II", 2f)
        )
    }
}