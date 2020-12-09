package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyHistoryAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.SemesterHistoryAdapter
import kotlinx.android.synthetic.main.fragment_semester_history.*

class SemesterHistoryFragment : Fragment(R.layout.fragment_semester_history) {

    //region immutable vars

    //region adapters
    private val semesterHistoryAdapter by lazy {
        SemesterHistoryAdapter(
            listOf(
                listOf(
                    Discipline("", "Química Orgânica", "", 5.1f),
                    Discipline("", "Álgebra Linear", "", 5.1f),
                    Discipline("", "Expressão Gráfica", "", 5.1f)
                ),
                listOf(
                    Discipline("", "Química Orgânica", "", 5.1f),
                    Discipline("", "Álgebra Linear", "", 5.1f),
                    Discipline("", "Expressão Gráfica", "", 5.1f)
                ),
                listOf(
                    Discipline("", "Química Orgânica", "", 5.1f),
                    Discipline("", "Álgebra Linear", "", 5.1f),
                    Discipline("", "Expressão Gráfica", "", 5.1f)
                )
            )
        )
    }
    private val emptyHistoryAdapter by lazy { EmptyHistoryAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(emptyHistoryAdapter) }
    //endregion

    //endregion

    //region lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        semester_history_recyclerview.adapter = concatAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as? HomeActivity)?.fab()?.gone()
    }
    //endregion
}