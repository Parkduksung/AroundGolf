package com.example.aroundgolf.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState

class HomeViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

//
//    fun addBookmarkItem(item: CampingItem) {
//        ioScope {
//            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
//
//                firebaseRepository.addBookmarkItem(userId, item).addOnCompleteListener { dbResult ->
//                    if (dbResult.isSuccessful) {
//                        viewStateChanged(HomeViewState.AddBookmarkItem(item))
//                    } else {
//                        viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
//                    }
//                }
//            }
//        }
//    }
//
//    fun deleteBookmarkItem(item: CampingItem) {
//        ioScope {
//            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
//
//                firebaseRepository.deleteBookmarkItem(userId, item)
//                    .addOnCompleteListener { dbResult ->
//                        if (dbResult.isSuccessful) {
//                            viewStateChanged(HomeViewState.DeleteBookmarkItem(item))
//                        } else {
//                            viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
//                        }
//                    }
//            }
//        }
//    }


    fun permissionGrant() {
        viewStateChanged(HomeViewState.PermissionGrant)
    }

    sealed class HomeViewState : ViewState {
        data class Error(val errorMessage: String) : HomeViewState()
//        data class AddBookmarkItem(val item: CampingItem) : HomeViewState()
//        data class DeleteBookmarkItem(val item: CampingItem) : HomeViewState()
        object PermissionGrant : HomeViewState()
    }
}