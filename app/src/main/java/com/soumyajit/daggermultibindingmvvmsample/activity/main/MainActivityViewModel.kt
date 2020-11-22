package com.soumyajit.daggermultibindingmvvmsample.activity.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soumyajit.daggermultibindingmvvmsample.*
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(
    private val mainActivityRepository: MainActivityRepository
) : ViewModel() {
    val TAG = MainActivityViewModel::class.java.simpleName
    private val _state : MutableLiveData<MainActivityViewState> = MutableLiveData()
    val state: LiveData<MainActivityViewState> = _state

    init {
        _state.postValue(MainActivityViewState.ShowLoading)
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            when(val response = mainActivityRepository.getData()){
                is ResultHandler.Error -> {
                    val throwable = response.throwable
                    Log.i(TAG,throwable.toString())
                    _state.postValue(
                        MainActivityViewState.ShowError(
                            throwable
                        )
                    )
                }
                is ResultHandler.Success -> {
                    val result = response.data
                    Log.i(TAG,result.toString())
                    _state.postValue(
                        MainActivityViewState.ShowData(
                            result.data
                        )
                    )
                }
            }
        }
    }
}