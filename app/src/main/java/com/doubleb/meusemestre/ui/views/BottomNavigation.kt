package com.doubleb.meusemestre.ui.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.updatePadding
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.view_bottom_navigation.view.*

class BottomNavigation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var listener: Listener? = null

    init {
        View.inflate(context, R.layout.view_bottom_navigation, this)

        updatePadding(top = resources.getDimension(R.dimen.spacing_sixteen).toInt())
        layoutTransition = LayoutTransition().apply {
            enableTransitionType(LayoutTransition.CHANGING)
        }

        bottom_navigation_button_dashboard
            .icon(R.drawable.vector_dashboard, R.color.dark_purple)
            .backgroundColorRes(R.color.light_purple)
            .title(R.string.bottom_navigation_dashboard)
            .open()
            .setOnClickListener(buttonClickListener(Type.DASHBOARD))

        bottom_navigation_button_disciplines
            .icon(R.drawable.vector_disciplines, R.color.dark_blue)
            .backgroundColorRes(R.color.light_blue)
            .title(R.string.bottom_navigation_disciplines)
            .setOnClickListener(buttonClickListener(Type.DISCIPLINES))

        bottom_navigation_button_tips
            .icon(R.drawable.vector_tips, R.color.dark_yellow)
            .backgroundColorRes(R.color.light_yellow)
            .title(R.string.bottom_navigation_tips)
            .setOnClickListener(buttonClickListener(Type.TIPS))
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun buttonClickListener(type: Type) = OnClickListener {
        this.children.toList().forEach { child ->
            (child as? ButtonSelectionView)?.close()
        }

        val button = it as ButtonSelectionView
        button.open()

        listener?.onNavigationItemSelected(button, type)
    }

    fun interface Listener {
        fun onNavigationItemSelected(view: View, type: Type)
    }

    enum class Type {
        DASHBOARD,
        DISCIPLINES,
        TIPS
    }
}