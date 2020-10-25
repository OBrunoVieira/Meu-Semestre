package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.BestDisciplineAdapter
import com.doubleb.meusemestre.ui.adapters.DisciplineListAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private val disciplineListAdapter by lazy { DisciplineListAdapter() }
    private val bestDisciplineAdapter by lazy { BestDisciplineAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(disciplineListAdapter, bestDisciplineAdapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

}