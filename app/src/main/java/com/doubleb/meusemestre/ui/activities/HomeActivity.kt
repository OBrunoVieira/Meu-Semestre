package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Subject
import com.doubleb.meusemestre.ui.adapters.BestSubjectAdapter
import com.doubleb.meusemestre.ui.adapters.SubjectListAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private val subjectsListAdapter by lazy { SubjectListAdapter() }
    private val bestSubjectAdapter by lazy { BestSubjectAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(subjectsListAdapter, bestSubjectAdapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboard_recycler_view.adapter = concatAdapter
        bestSubjectAdapter.subject = Subject("", "Fotografia", 10f)


        subjectsListAdapter.list = listOf(
            Subject("", "Fotografia", 10f),
            Subject("", "Sociologia", 2f),
            Subject("", "Desenvolvimento de Banco de Dados II", 2f),
            Subject("", "Química", 5f),
            Subject("", "Álgebra Linear", 3.4f),
            Subject("", "Cálculo I", 7f),
            Subject("", "Ilustração", 9f),
            Subject("", "Literatura Moderna", 9.5f),
            Subject("", "Literatura Clássica", 8f)
        )
    }

}