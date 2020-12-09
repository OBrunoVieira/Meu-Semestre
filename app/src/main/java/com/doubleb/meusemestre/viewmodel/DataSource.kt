package com.doubleb.meusemestre.viewmodel

data class DataSource<T>(
    val dataState: DataState,
    val data: T? = null,
    val throwable: Throwable? = null
)

enum class DataState {
    LOADING,
    SUCCESS,
    ERROR
}