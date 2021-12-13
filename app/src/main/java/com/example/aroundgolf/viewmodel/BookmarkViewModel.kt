package com.example.aroundgolf.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.ext.ioScope


class BookmarkViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {


    fun getAllBookmark() {
        ioScope {
//            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
//                firebaseRepository.getFirebaseFireStore().collection(userId)
//                    .document("camping")
//                    .get().addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            if (it.result.exists()) {
//
//                                val getResult: ArrayList<HashMap<String, String>>? =
//                                    it.result.get("like") as ArrayList<HashMap<String, String>>?
//
//                                val toResultList = getResult?.map { it.toCampingItem() }
//
//                                if (!toResultList.isNullOrEmpty()) {
//                                    viewStateChanged(BookmarkViewState.BookmarkList(toResultList))
//                                } else {
//                                    viewStateChanged(BookmarkViewState.EmptyBookmarkList)
//                                }
//                            } else {
//                                viewStateChanged(BookmarkViewState.EmptyBookmarkList)
//                            }
//                        } else {
//                            viewStateChanged(BookmarkViewState.Error(it.exception?.message.toString()))
//                        }
//                    }
//            }
        }
    }



    sealed class BookmarkViewState : ViewState {
//        data class BookmarkList(val bookmarkList: List<CampingItem>) : BookmarkViewState()
        data class Error(val errorMessage: String) : BookmarkViewState()
        object EmptyBookmarkList : BookmarkViewState()
    }

}
