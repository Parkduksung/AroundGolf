package com.example.aroundgym.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.aroundgym.base.BaseViewModel
import com.example.aroundgym.base.ViewState
import com.example.aroundgym.data.model.BookItem
import com.example.aroundgym.data.repo.KAKAORepository
import com.example.aroundgym.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    app: Application,
    private val kakaoRepository: KAKAORepository
) : BaseViewModel(app) {

    val inputSearchEditText = MutableLiveData<String>()

    private var pageCount = INIT_PAGE_COUNT
    private var isLoad = false
    private var lastBookName: String? = null

    fun search() {
        initConfiguration()
        inputSearchEditText.value?.let { bookName ->

            ioScope.launch {
                when (val result = kakaoRepository.searchBook(
                    bookName = bookName,
                    size = DEFAULT_SEARCH_BOOK_SIZE,
                    sort = DEFAULT_SEARCH_BOOK_SORT,
                    page = pageCount
                )) {
                    is Result.Success -> {
                        if (result.data.meta.is_end) {
                            viewStateChanged(SearchViewState.PageEnd)
                        } else {
                            pageCount++
                            lastBookName = bookName
                            isLoad = true
                        }
                        viewStateChanged(SearchViewState.GetSearchBookList(result.data.documents.map { it.toBookItem() }))
                    }

                    is Result.Error -> {
                        viewStateChanged(SearchViewState.Error(result.exception.message.toString()))
                    }
                }
            }
        } ?: viewStateChanged(SearchViewState.EmptyInput)
    }

    private fun loadNextPage() {
        isLoad = false
        if (lastBookName != null && pageCount != INIT_PAGE_COUNT) {
            ioScope.launch {

                when (val result = kakaoRepository.searchBook(
                    bookName = lastBookName!!,
                    size = DEFAULT_SEARCH_BOOK_SIZE,
                    sort = DEFAULT_SEARCH_BOOK_SORT,
                    page = pageCount
                )) {

                    is Result.Success -> {
                        if (result.data.meta.is_end) {
                            initConfiguration()
                            viewStateChanged(SearchViewState.PageEnd)
                        } else {
                            pageCount++
                            isLoad = true
                        }
                        viewStateChanged(SearchViewState.GetLoadNextPageList(result.data.documents.map { it.toBookItem() }))
                    }

                    is Result.Error -> {
                        viewStateChanged(SearchViewState.Error(result.exception.message.toString()))
                    }
                }
            }
        }
    }

    private fun initConfiguration() {
        pageCount = INIT_PAGE_COUNT
        lastBookName = null
        isLoad = false
    }

    fun onScrolled(lastVisible: Int, itemCount: Int) {
        if ((lastVisible >= itemCount - 1) && isLoad) {
            loadNextPage()
        }
    }

    sealed class SearchViewState : ViewState {
        data class GetSearchBookList(val list: List<Pair<BookItem, Boolean>>) : SearchViewState()
        data class GetLoadNextPageList(val list: List<Pair<BookItem, Boolean>>) : SearchViewState()
        data class Error(val message: String) : SearchViewState()
        object PageEnd : SearchViewState()
        object EmptyInput : SearchViewState()
    }


    companion object {
        private const val INIT_PAGE_COUNT = 1
        private const val DEFAULT_SEARCH_BOOK_SIZE = 50
        private const val DEFAULT_SEARCH_BOOK_SORT = "accuracy"
    }
}