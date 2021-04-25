package com.doubleb.meusemestre.extensions

fun generateRandomString() = run {
    val maxLength = (20..30).random()
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    (1..maxLength)
        .map { allowedChars.random() }
        .joinToString("")
}

fun String?.takeIfValid() = this.takeIf { it.isValid() }

fun String?.isValid() = !this.isNullOrEmpty()