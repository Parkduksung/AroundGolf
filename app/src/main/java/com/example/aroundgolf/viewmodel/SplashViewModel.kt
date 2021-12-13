package com.example.aroundgolf.viewmodel

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.data.repo.GolfRepository
import com.example.aroundgolf.ext.ioScope
import com.example.aroundgolf.utils.Result
import org.koin.java.KoinJavaComponent

class SplashViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val golfRepository by KoinJavaComponent.inject<GolfRepository>(GolfRepository::class.java)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun start() {
        ioScope {
            when (val result = golfRepository.getAll()) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        viewStateChanged(MapViewModel.MapViewState.ShowProgress)
                        golfRepository.getGolfList { getGolfListResult ->
                            when (getGolfListResult) {
                                is Result.Success -> {
                                    val golfEntityList =
                                        getGolfListResult.data.GolfPlace[1].row.map { it.toGolfEntity() }

                                    ioScope {
                                        if (golfRepository.registerAll(golfEntityList)) {
                                            viewStateChanged(SplashViewState.RouteMain)
                                        } else {
                                            viewStateChanged(SplashViewState.Error)
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    viewStateChanged(SplashViewState.Error)
                                }
                            }
                        }
                    } else {
                        viewStateChanged(SplashViewState.RouteMain)
                    }
                }
                is Result.Error -> {
                    viewStateChanged(SplashViewState.Error)
                }
            }
        }
    }

    sealed class SplashViewState : ViewState {
        object LoadData : SplashViewState()
        object Error : SplashViewState()
        object RouteMain : SplashViewState()
    }


}