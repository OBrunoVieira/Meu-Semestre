package com.doubleb.meusemestre.ui.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.gone
import com.doubleb.meusemestre.extensions.tintBackgroundColor
import com.doubleb.meusemestre.extensions.tintColor
import com.doubleb.meusemestre.extensions.visible
import kotlinx.android.synthetic.main.view_button_selection.view.*

class ButtonSelectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val backgroundDrawable by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.shape_button_selection
        )
    }

    init {
        View.inflate(context, R.layout.view_button_selection, this)
        layoutTransition = LayoutTransition()
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        updatePadding(
            resources.getDimension(R.dimen.spacing_sixteen).toInt(),
            resources.getDimension(R.dimen.spacing_two).toInt(),
            resources.getDimension(R.dimen.spacing_sixteen).toInt(),
            resources.getDimension(R.dimen.spacing_two).toInt()
        )

        setOnClickListener {
            button_selection_text_view_action.isVisible =
                !button_selection_text_view_action.isVisible
        }

        close()
    }

    fun close() = apply {
        button_selection_text_view_action.gone()
        setBackgroundResource(0)
    }

    fun open() = apply {
        button_selection_text_view_action.visible()
        background = backgroundDrawable
    }

    fun backgroundColorRes(@ColorRes color: Int) = apply {
        tintBackgroundColor(ContextCompat.getColor(context, color))
    }

    fun icon(@DrawableRes drawable: Int, @ColorRes tint: Int = -1) = apply {
        button_selection_image_view_icon.setImageResource(drawable)
        button_selection_text_view_action.setTextColor(ContextCompat.getColor(context, tint))
        tint.takeIf { it != -1 }?.let { button_selection_image_view_icon.tintColor(tint) }
    }

    fun title(@StringRes string: Int) = apply {
        button_selection_text_view_action.setText(string)
    }

}