package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.ui.activities.DisciplineRegistrationActivity
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ActiveSemesterAdapter
import com.doubleb.meusemestre.ui.adapters.recyclerview.EmptyDisciplinesAdapter
import com.doubleb.meusemestre.ui.listeners.DisciplineListener
import com.doubleb.meusemestre.ui.views.EmptyStateView
import kotlinx.android.synthetic.main.fragment_active_semester.*

class ActiveSemesterFragment : Fragment(R.layout.fragment_active_semester), DisciplineListener,
    EmptyStateView.ClickListener {

    //region immutable vars

    //region adapters
    private val activeSemesterAdapter by lazy { ActiveSemesterAdapter(this) }
    private val emptyDisciplinesAdapter by lazy { EmptyDisciplinesAdapter(this) }
    private val concatAdapter by lazy { ConcatAdapter(emptyDisciplinesAdapter) }
    //endregion

    //region components
    private val fab by lazy { (activity as? HomeActivity)?.fab() }
    //endregion

    //region listeners
    private val clickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(it.context, DisciplineRegistrationActivity::class.java))
        }
    }
    //endregion

    //endregion

    //region lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        active_semester_recycler_view.adapter = concatAdapter
    }

    override fun onResume() {
        super.onResume()
        fab?.let {
            it.setOnClickListener(clickListener)
            it.setText(R.string.active_semester_fab)

            if (activeSemesterAdapter.currentList.isNotEmpty()) {
                it.show()
                it.extend()
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

    override fun onEmptyViewActionClick(view: View) {
        startActivity(Intent(view.context, DisciplineRegistrationActivity::class.java))
    }
    //endregion
}