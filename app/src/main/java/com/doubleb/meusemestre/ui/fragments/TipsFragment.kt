package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.visible
import com.doubleb.meusemestre.models.Tip
import com.doubleb.meusemestre.ui.adapters.recyclerview.TipsAdapter
import com.doubleb.meusemestre.viewmodel.DataSource
import com.doubleb.meusemestre.viewmodel.DataState
import com.doubleb.meusemestre.viewmodel.TipsViewModel
import kotlinx.android.synthetic.main.fragment_tips.*
import org.koin.android.ext.android.inject

class TipsFragment : Fragment(R.layout.fragment_tips) {

    //region immutable vars

    //region adapters
    private val adapter by lazy { TipsAdapter() }
    //endregion

    //region viewModels
    private val tipsViewModel: TipsViewModel by inject()
    //endregion

    //endregion

    //region lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tipsViewModel.livedata.observe(viewLifecycleOwner, observeTips())

        tips_recycler_view.adapter = adapter
        tipsViewModel.getTips()
    }
    //endregion

    //region observers
    private fun observeTips() = Observer<DataSource<List<Tip>>> {
        when (it.dataState) {
            DataState.LOADING -> {
                tips_progress.visible()
                tips_recycler_view.gone()
            }

            DataState.SUCCESS -> {
                tips_progress.gone()
                tips_recycler_view.visible()
                it.data?.let { data ->
                    adapter.tips = data
                    adapter.notifyDataSetChanged()
                }
            }

            DataState.ERROR -> {
                tips_progress.gone()
                tips_recycler_view.visible()
            }
        }
    }
    //endregion

}