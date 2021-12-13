package com.example.aroundgym.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    private val _viewStateLiveData = MutableLiveData<ViewState>()
    val viewStateLiveData: LiveData<ViewState> = _viewStateLiveData

    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val ioScope = CoroutineScope(Dispatchers.IO)

    protected fun viewStateChanged(viewState: ViewState) {
        uiScope.launch {
            _viewStateLiveData.value = viewState
            _viewStateLiveData.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
        ioScope.cancel()
    }

}

interface ViewState