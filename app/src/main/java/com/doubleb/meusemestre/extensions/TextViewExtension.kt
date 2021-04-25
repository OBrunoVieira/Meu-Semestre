package com.doubleb.meusemestre.extensions

import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.drawableLeft(@DrawableRes drawable: Int) =
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)

fun TextView.showIfValidText(text: String?) =
    text.takeIf { !it.isNullOrEmpty() }?.let {
        this.visible()
        this.text = text
    } ?: run {
        this.gone()
    }

fun TextView.setTextIfValid(text:String?) = run {
    setText(text.takeIfValid() ?: this.text)
}

fun TextView.disableCopyPaste() {
    val callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?) = run {
            menu?.clear()
            false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = run {
            menu?.clear()
            false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = false

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        customInsertionActionModeCallback = callback
    }
    customSelectionActionModeCallback = callback

    setTextIsSelectable(false)
    isLongClickable = false
}