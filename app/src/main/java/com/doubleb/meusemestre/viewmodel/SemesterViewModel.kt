package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.extensions.generateRandomString
import com.doubleb.meusemestre.repository.SemesterRepository
import com.doubleb.meusemestre.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class  SemesterViewModel(
    private val repository: SemesterRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val livedata = MutableLiveData<DataSource<String>>()

    fun createSemester(name: String) {
        livedata.postValue(DataSource(DataState.LOADING))

        val semesterId = generateRandomString()

        viewModelScope.launch {
            val createSemester = repository.createSemester(name, semesterId)
                .addOnCanceledListener {
                    livedata.postValue(DataSource(DataState.ERROR))
                }
                .addOnFailureListener {
                    livedata.postValue(DataSource(DataState.ERROR, throwable = it))
                }
                .addOnSuccessListener {
                    livedata.postValue(DataSource(DataState.SUCCESS, semesterId))
                }

            awaitAll(
                async { createSemester },
                async { userRepository.updateUser(currentSemester = semesterId) }
            )
        }
    }

}