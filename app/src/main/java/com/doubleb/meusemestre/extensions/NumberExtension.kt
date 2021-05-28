package com.doubleb.meusemestre.extensions

import java.text.DecimalFormat

fun Float.roundWhenBase10(): String =
    DecimalFormat(if (this % 1 == 0f) "#" else "#.#").format(this)

fun Double.roundWhenBase10(): String =
    DecimalFormat(if (this % 1 == 0.0) "#" else "#.#").format(this)

fun Int.toRGB() = 255 * this / 100