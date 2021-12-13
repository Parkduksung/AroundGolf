package com.example.aroundgym.ui

import android.app.Application
import com.example.aroundgym.base.BaseViewModel
import com.example.aroundgym.base.ViewState
import com.example.aroundgym.data.model.BookItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(app: Application) : BaseViewModel(app) {


    fun routSearch() {
        viewStateChanged(MainViewState.RouteSearch)
    }

    fun routeDetail(item: Pair<BookItem, Boolean>) {
        viewStateChanged(MainViewState.RouteDetail(item))
    }

    fun toggleLike(item: Pair<BookItem, Boolean>) {
        viewStateChanged(MainViewState.ToggleLike(item))
    }

    sealed class MainViewState : ViewState {
        object RouteSearch : MainViewState()
        data class RouteDetail(val item: Pair<BookItem, Boolean>) : MainViewState()
        data class ToggleLike(val item: Pair<BookItem, Boolean>) : MainViewState()
    }
}