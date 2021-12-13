package com.example.aroundgolf.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.ext.ioScope

class SplashViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {


    private fun start() {
        ioScope {
//            if (goCampingRepository.checkExistCampingData()) {
//                viewStateChanged(SplashViewState.RouteMain)
//            } else {
//                viewStateChanged(SplashViewState.LoadData)
//                goCampingRepository.getBasedList(
//                    onSuccess = {
//                        val toCampingEntity =
//                            it.basedResponse.basedListBody.basedListItems.basedListItem.map { it.toCampingEntity() }
//
//                        ioScope {
//                            if (goCampingRepository.registerCampingList(toCampingEntity)) {
//                                viewStateChanged(SplashViewState.RouteMain)
//                            } else {
//                                viewStateChanged(SplashViewState.Error)
//                            }
//                        }
//                    },
//                    onFailure = {
//                        viewStateChanged(SplashViewState.Error)
//                    }
//                )
//            }
        }
    }

    sealed class SplashViewState : ViewState {
        object LoadData : SplashViewState()
        object Error : SplashViewState()
        object RouteMain : SplashViewState()
    }


}