package com.doubleb.meusemestre.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.models.Exam
import com.doubleb.meusemestre.ui.activities.ExamRegistrationActivity
import com.doubleb.meusemestre.ui.activities.HomeActivity
import com.doubleb.meusemestre.ui.adapters.recyclerview.ExamAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_discipline_details.*

class DisciplineDetailsFragment : Fragment(R.layout.fragment_discipline_details) {

    companion object {
        private const val DISCIPLINE_DETAILS_NAME = "DISCIPLINE_DETAILS_TITLE"
        private const val DISCIPLINE_DETAILS_GRADE = "DISCIPLINE_DETAILS_GRADE"

        fun instance(title: String?, grade: Float?) = DisciplineDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(DISCIPLINE_DETAILS_NAME, title)
                putFloat(DISCIPLINE_DETAILS_GRADE, grade ?: -1f)
            }
        }
    }

    private var grade: Float? = null
    private var name: String? = null

    private val adapter by lazy { ExamAdapter() }
    private val clickListener by lazy {
        View.OnClickListener {
            startActivity(Intent(it.context, ExamRegistrationActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = arguments?.getString(DISCIPLINE_DETAILS_NAME)
        grade = arguments?.getFloat(DISCIPLINE_DETAILS_GRADE)

        discipline_details_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val alpha = discipline_details_collapsing_toolbar.contentScrim?.alpha ?: 0
            if (alpha in 200..255) {
                (activity as? HomeActivity)?.supportActionBar?.title = name
            } else {
                (activity as? HomeActivity)?.supportActionBar?.title = " "
            }
        })

        (activity as? HomeActivity)?.run {
            setSupportActionBar(discipline_details_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            hideNavigation()
        }

        discipline_details_recyclerview.adapter = adapter
        adapter.submitList(
            listOf(
                Exam("", "AV1", 5.1f),
                Exam("", "APS", null),
                Exam("", "AV2", 5.9f)
            )
        )

        discipline_details_average_indicator.title(name).average(grade)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? HomeActivity)?.run {
            showNavigation()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as? HomeActivity)?.fab()?.let {
            it.setOnClickListener(clickListener)
            it.setText(R.string.discipline_detail_fab)
            it.show()
            it.extend()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as? HomeActivity)?.fab()?.let {
            it.gone()
            it.setOnClickListener(null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_discipline_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_discipline_detail_delete) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}