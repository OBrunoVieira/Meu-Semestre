package com.doubleb.meusemestre.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.models.Tip
import com.doubleb.meusemestre.ui.adapters.recyclerview.TipsAdapter
import kotlinx.android.synthetic.main.fragment_tips.*

class TipsFragment : Fragment(R.layout.fragment_tips) {

    val adapter by lazy {
        TipsAdapter(
            List(3) {
                Tip(
                    "Dica $it",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris luctus, enim et ornare."
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tips_recycler_view.adapter = adapter
    }

}