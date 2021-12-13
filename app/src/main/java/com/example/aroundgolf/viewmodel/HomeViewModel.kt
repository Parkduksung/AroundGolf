package com.example.aroundgolf.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.data.repo.GolfRepository
import com.example.aroundgolf.ext.ioScope
import com.example.aroundgolf.room.GolfEntity
import com.example.aroundgolf.utils.Result
import org.koin.java.KoinJavaComponent

class HomeViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val golfRepository by KoinJavaComponent.inject<GolfRepository>(GolfRepository::class.java)

    fun addBookmarkItem(item: GolfEntity) {
        ioScope {

            when (val result = golfRepository.toggleBookmarkGolf(item)) {

                is Result.Success -> {
                    viewStateChanged(HomeViewState.AddBookmarkItem(result.data))
                }

                is Result.Error -> {
                    viewStateChanged(HomeViewState.Error("addBookmarkItem Error"))
                }
            }
        }
    }

    fun deleteBookmarkItem(item: GolfEntity) {
        ioScope {
            when (val result = golfRepository.toggleBookmarkGolf(item)) {
                is Result.Success -> {
                    viewStateChanged(HomeViewState.DeleteBookmarkItem(result.data))
                }

                is Result.Error -> {
                    viewStateChanged(HomeViewState.Error("addBookmarkItem Error"))
                }
            }
        }
    }


    fun permissionGrant() {
        viewStateChanged(HomeViewState.PermissionGrant)
    }

    sealed class HomeViewState : ViewState {
        data class Error(val errorMessage: String) : HomeViewState()
        data class AddBookmarkItem(val item: GolfEntity) : HomeViewState()
        data class DeleteBookmarkItem(val item: GolfEntity) : HomeViewState()
        object PermissionGrant : HomeViewState()
    }
}