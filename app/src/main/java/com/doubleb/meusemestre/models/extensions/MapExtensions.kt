package com.doubleb.meusemestre.models.extensions

import com.doubleb.meusemestre.models.Exam

fun Map<String, List<Exam>?>.hasPendingGrades() =
    this.takeIf { it.isNotEmpty() }
        ?.any { it.value.hasPendingGrades() }