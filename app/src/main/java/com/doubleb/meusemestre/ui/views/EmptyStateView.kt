package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.showIfValidDrawable
import com.doubleb.meusemestre.extensions.showIfValidText
import kotlinx.android.synthetic.main.view_empty_state.view.*

class EmptyStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var titleText: String?
    private var descriptionText: String?
    private var buttonText: String?
    private var image: Drawable?

    init {
        View.inflate(context, R.layout.view_empty_state, this)
        gravity = Gravity.CENTER
        orientation = VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.EmptyStateView, defStyleAttr, 0)
            .run {
                titleText = getString(R.styleable.EmptyStateView_esv_title)
                descriptionText = getString(R.styleable.EmptyStateView_esv_description)
                buttonText = getString(R.styleable.EmptyStateView_esv_button_text)
                image = getDrawable(R.styleable.EmptyStateView_esv_src)
                recycle()
            }
    }

    fun listener(listener: ClickListener) =
        empty_state_button.setOnClickListener { listener.onEmptyViewActionClick(it) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        empty_state_image_view.showIfValidDrawable(image)
        empty_state_text_view_title.showIfValidText(titleText)
        empty_state_text_view_description.showIfValidText(descriptionText)
        empty_state_button.showIfValidText(buttonText)
    }

    fun interface ClickListener {
        fun onEmptyViewActionClick(view:View)
    }
}