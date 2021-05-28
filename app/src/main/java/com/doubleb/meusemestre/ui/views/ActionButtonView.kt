package com.doubleb.meusemestre.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.*
import kotlinx.android.synthetic.main.view_action_button.view.*

class ActionButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var title: String = ""
    private var listener: OnClickListener? = null

    init {
        inflate(context, R.layout.view_action_button, this)

        context.obtainStyledAttributes(attrs, R.styleable.ActionButtonView, defStyleAttr, 0)
            .run {
                title = getString(R.styleable.ActionButtonView_abv_title_button).orEmpty()
                recycle()
            }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        actionText(title)
        action_button.setOnClickListener {
            state(State.LOADING)
            listener?.onClick(it)
        }

    }

    fun listener(listener: OnClickListener) {
        this.listener = listener
    }

    fun actionText(text: String) = apply {
        action_button.text = text
    }

    fun isEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            enable()
        } else {
            disable()
        }
    }

    fun state(state: State) {
        if (state == State.LOADING) {
            buildLoadingState()
            return
        }

        buildDefaultState()
    }

    private fun buildLoadingState() {
        action_button.invisible()
        action_button_progress_bar.visible()
    }

    private fun buildDefaultState() {
        action_button.visible()
        action_button_progress_bar.gone()
    }

    enum class State {
        LOADING,
        DEFAULT
    }

}