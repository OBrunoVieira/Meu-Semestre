package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ExamAdapter
import kotlinx.android.synthetic.main.fragment_discipline_details.*

class DisciplineDetailsFragment : Fragment(R.layout.fragment_discipline_details) {

    companion object {
        private const val DISCIPLINE_DETAILS_TITLE = "DISCIPLINE_DETAILS_TITLE"
        private const val DISCIPLINE_DETAILS_GRADE = "DISCIPLINE_DETAILS_GRADE"

        fun instance(title: String?, grade: Float?) = DisciplineDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(DISCIPLINE_DETAILS_TITLE, title)
                putFloat(DISCIPLINE_DETAILS_GRADE, grade ?: -1f)
            }
        }
    }

    private var grade: Float? = null
    private var title: String? = null

    private val adapter by lazy { ExamAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? HomeActivity)?.run {
            setSupportActionBar(discipline_details_toolbar)

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }

            hideNavigation()
        }

        title = arguments?.getString(DISCIPLINE_DETAILS_TITLE)
        grade = arguments?.getFloat(DISCIPLINE_DETAILS_GRADE)

        discipline_details_recyclerview.adapter = adapter
        adapter.submitList(
            listOf(
                Exam("", "AV1", 5.1f),
                Exam("", "APS", null),
                Exam("", "AV2", 5.9f)
            )
        )

        discipline_details_average_indicator.title(title).average(grade)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? HomeActivity)?.run {
            showNavigation()
        }
    }

}