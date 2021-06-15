package com.doubleb.meusemestre.models.extensions

import com.doubleb.meusemestre.models.Exam

fun List<Exam>?.sumGradeResultByCycle(cycle:Int) =
    this?.filter { exam -> exam.cycle == cycle }?.mapNotNull { it.grade_result }?.takeIf { it.isNotEmpty() }?.sum()

fun List<Exam>?.groupByCycle() =
    this?.sortedBy { it.cycle }
        ?.groupBy { it.cycle }
        ?.toList()

fun List<Pair<Int?, List<Exam>>>.transformToGradeList() =
    this
        .mapNotNull { pair ->
            val resultList = pair.second.mapNotNull { exam -> exam.grade_result }
            resultList.takeIf { it.isNotEmpty() }?.sum()
        }

fun List<Pair<Int?, List<Exam>>>.transformToAverageList() =
    this.transformToGradeList()
        .sortedDescending()
        .take(2)

fun List<Pair<Int?, List<Exam>>>.transformToVariationList() =
    run {
        val resultList = this.mapNotNull { pair ->
            val grades = pair.second.mapNotNull { exam -> exam.grade_result }
            grades.takeIf { it.isNotEmpty() }?.sum()
        }

        if (resultList.size >= 2) {
            val diffList = resultList.takeLast(2)
            diffList.last() - diffList.first()
        } else {
            null
        }
    }

fun List<Exam>?.hasPendingGrades() =
    this?.takeIf { it.isNotEmpty() }
        ?.any { it.grade_result == null } ?: true