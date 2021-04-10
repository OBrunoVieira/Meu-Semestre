package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleb.meusemestre.models.Tip
import com.doubleb.meusemestre.repository.TipsRepository

class TipsViewModel(private val tipsRepository: TipsRepository) : ViewModel() {

    val livedata = MutableLiveData<DataSource<List<Tip>>>()

    fun getTips() {
        livedata.postValue(DataSource(DataState.LOADING))


        tipsRepository.getTips({
            livedata.postValue(DataSource(DataState.SUCCESS, it))
        }, {
            livedata.postValue(DataSource(DataState.ERROR))
        })
    }

}