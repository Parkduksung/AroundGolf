package com.example.aroundgym.ui

import base.ViewModelBaseTest
import com.example.aroundgym.di.ApiModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class MainViewModelTest : ViewModelBaseTest() {

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        mainViewModel = MainViewModel(application)
        mainViewModel.viewStateLiveData.observeForever(viewStateObserver)
    }


    @Test
    fun checkRouteSearchTest() = runBlocking {

        mainViewModel.routSearch()

        Mockito.verify(viewStateObserver).onChanged(MainViewModel.MainViewState.RouteSearch)

    }

    @Test
    fun checkToggleLikeTest() = runBlocking {

        val mockItem = ApiModule.provideKakaoApi().getSearchBook(
            query = "안드로이드",
            size = 50,
            page = 1,
            sort = "accuracy"
        ).documents[0].toBookItem()

        mainViewModel.toggleLike(mockItem)

        Mockito.verify(viewStateObserver)
            .onChanged(MainViewModel.MainViewState.ToggleLike(mockItem))

    }

    @Test
    fun checkRouteDetailTest() = runBlocking {

        val mockItem = ApiModule.provideKakaoApi().getSearchBook(
            query = "안드로이드",
            size = 50,
            page = 1,
            sort = "accuracy"
        ).documents[0].toBookItem()

        mainViewModel.routeDetail(mockItem)

        Mockito.verify(viewStateObserver)
            .onChanged(MainViewModel.MainViewState.RouteDetail(mockItem))

    }
}