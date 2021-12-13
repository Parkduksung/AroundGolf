package com.example.aroundgym.ui

import base.ViewModelBaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class DetailViewModelTest : ViewModelBaseTest() {

    private lateinit var detailViewModel: DetailViewModel

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        detailViewModel = DetailViewModel(application)
        detailViewModel.viewStateLiveData.observeForever(viewStateObserver)
    }


    @Test
    fun checkRouteSearch() = runBlocking {

        detailViewModel.routeSearch()

        Mockito.verify(viewStateObserver).onChanged(DetailViewModel.DetailViewState.RouteSearch)

    }
}