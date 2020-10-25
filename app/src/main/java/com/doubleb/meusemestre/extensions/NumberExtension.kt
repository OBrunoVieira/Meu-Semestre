package com.doubleb.meusemestre.extensions

import java.text.DecimalFormat

fun Float.roundWhenBase10(): String =
    DecimalFormat(if (this % 1 == 0f) "#" else "#.#").format(this)