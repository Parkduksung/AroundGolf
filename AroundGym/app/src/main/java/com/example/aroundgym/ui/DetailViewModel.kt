package com.example.aroundgym.ui

import android.app.Application
import com.example.aroundgym.base.BaseViewModel
import com.example.aroundgym.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

    fun routeSearch() {
        viewStateChanged(DetailViewState.RouteSearch)
    }

    sealed class DetailViewState : ViewState {

        object RouteSearch : DetailViewState()
    }
}