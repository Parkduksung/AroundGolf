package com.rsupport.rv.agent.myapplication

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel(), LifecycleObserver {

    private val _mainViewState = MutableLiveData<MainViewState>()
    val mainViewState: LiveData<MainViewState> = _mainViewState

    val inputAEditText = MutableLiveData<String>()
    val inputBEditText = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun delays() {

        viewModelScope.launch(Dispatchers.IO) {

            delay(2000L)

            withContext(Dispatchers.Main) {
                _mainViewState.value = MainViewState.Delay
            }
        }
    }

    fun add() {
        _mainViewState.value = MainViewState.Plus(
            inputAEditText.value!!.toInt() + inputBEditText.value!!.toInt()
        )
    }

    fun delays2() {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000L)

            withContext(Dispatchers.Main) {
                _mainViewState.value = MainViewState.Delay1
            }
        }
    }

    sealed class MainViewState {
        data class Plus(val result: Int) : MainViewState()
        object Delay : MainViewState()
        object Delay1 : MainViewState()
        object Min : MainViewState()
    }
}



