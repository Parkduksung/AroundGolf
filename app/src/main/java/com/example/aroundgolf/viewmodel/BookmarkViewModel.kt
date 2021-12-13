package com.example.aroundgolf.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.data.repo.GolfRepository
import com.example.aroundgolf.ext.ioScope
import com.example.aroundgolf.room.GolfEntity
import com.example.aroundgolf.utils.Result
import org.koin.java.KoinJavaComponent


class BookmarkViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val golfRepository by KoinJavaComponent.inject<GolfRepository>(GolfRepository::class.java)

    fun getAllBookmark() {
        ioScope {
            when (val result = golfRepository.getAllBookmarkList()) {

                is Result.Success -> {
                    Log.d("결과", result.data.size.toString())
//                    viewStateChanged(HomeViewModel.HomeViewState.AddBookmarkItem(result.data))
                }

                is Result.Error -> {
//                    viewStateChanged(HomeViewModel.HomeViewState.Error("addBookmarkItem Error"))
                }
            }
        }
    }

    sealed class BookmarkViewState : ViewState {
        data class BookmarkList(val bookmarkList: List<GolfEntity>) : BookmarkViewState()
        data class Error(val errorMessage: String) : BookmarkViewState()
        object EmptyBookmarkList : BookmarkViewState()
    }

}
