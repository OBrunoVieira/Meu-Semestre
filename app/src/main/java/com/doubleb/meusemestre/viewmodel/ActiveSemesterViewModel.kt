package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubleb.meusemestre.models.ActiveSemester
import com.doubleb.meusemestre.models.Discipline
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.repository.DisciplinesRepository
import com.doubleb.meusemestre.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ActiveSemesterViewModel(
    private val userRepository: UserRepository,
    private val disciplinesRepository: DisciplinesRepository
) : ViewModel() {

    val liveData = MutableLiveData<DataSource<ActiveSemester>>()

    fun getActiveSemester() {
        liveData.postValue(DataSource(DataState.LOADING))

        viewModelScope.launch {
            val userData = async { userRepository.getSuspendedUser() }
            val disciplinesData = async { disciplinesRepository.getSuspendedDisciplines() }

            val result = awaitAll(userData, disciplinesData)

            val user = result[0] as? User?
            val disciplines = result[1] as? ArrayList<Discipline>?

            if (user == null && disciplines == null || user == null) {
                liveData.postValue(DataSource(DataState.ERROR))
                return@launch
            }

            liveData.postValue(DataSource(DataState.SUCCESS, ActiveSemester(user, disciplines)))
        }
    }

}