package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ActiveSemesterAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptySemesterAdapter
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import kotlinx.android.synthetic.main.fragment_active_semester.*

class ActiveSemesterFragment : Fragment(R.layout.fragment_active_semester), DisciplineListener,
    EmptyStateView.ClickListener {

    private val adapter by lazy { ActiveSemesterAdapter(this) }
    private val emptyDisciplinesAdapter by lazy { EmptySemesterAdapter(this) }
    private val concatAdapter by lazy { ConcatAdapter(emptyDisciplinesAdapter) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        active_semester_recycler_view.adapter = concatAdapter
    }

    override fun onDisciplineClick(position: Int) {
        (activity as? HomeActivity)?.inflateStackFragment(
            DisciplineDetailsFragment.instance(
                adapter.currentList[position].name,
                adapter.currentList[position].grade
            )
        )
    }

    override fun onEmptyViewActionClick(view: View) {
        startActivity(Intent(view.context, DisciplineRegistrationActivity::class.java))
    }
}