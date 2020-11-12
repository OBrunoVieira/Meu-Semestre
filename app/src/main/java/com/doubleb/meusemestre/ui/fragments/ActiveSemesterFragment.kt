package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.adapters.recyclerview.ActiveSemesterAdapter
import kotlinx.android.synthetic.main.fragment_active_semester.*

class ActiveSemesterFragment : Fragment(R.layout.fragment_active_semester) {

    val adapter by lazy { ActiveSemesterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        active_semester_recycler_view.adapter = adapter

        adapter.submitList(arrayListOf<Discipline>().apply {
            add(Discipline("", "Fotografia", null))
            add(Discipline("", "Desenvolvimento de Banco de Dados", 3.2f))
            add(Discipline("", "Programação Orientada à Objetos", 7f))
            add(Discipline("", "Cálculo III", 10f))
            add(Discipline("", "Física I", 9.3f))
            add(Discipline("", "Química Orgânica", null))
            add(Discipline("", "Filosofia", null))
        })
    }
}