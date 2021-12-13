package com.example.aroundgym.ui

import base.ViewModelBaseTest
import com.example.aroundgym.api.response.Meta
import com.example.aroundgym.data.repo.KAKAORepository
import com.example.aroundgym.di.ApiModule
import com.example.aroundgym.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class SearchViewModelTest : ViewModelBaseTest() {

    @Mock
    lateinit var kakaoRepository: KAKAORepository

    private lateinit var searchViewModel: SearchViewModel


    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        kakaoRepository = Mockito.mock(KAKAORepository::class.java)
        searchViewModel = SearchViewModel(application, kakaoRepository)
        searchViewModel.viewStateLiveData.observeForever(viewStateObserver)
    }

    @Test
    fun checkGetSearchBookListTest() = runBlocking {

        val mockItem = ApiModule.provideKakaoApi().getSearchBook(
            query = "안드로이드",
            size = 50,
            page = 1,
            sort = "accuracy"
        )
        Mockito.`when`(
            kakaoRepository.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(Result.Success(mockItem))

        searchViewModel.inputSearchEditText.value = "안드로이드"

        searchViewModel.search()

        delay(100L)

        Mockito.verify(viewStateObserver)
            .onChanged(SearchViewModel.SearchViewState.GetSearchBookList(mockItem.documents.map { it.toBookItem() }))

    }

    @Test
    fun checkPageEndTest() = runBlocking {

        val mockItem = ApiModule.provideKakaoApi().getSearchBook(
            query = "안드로이드",
            size = 50,
            page = 1,
            sort = "accuracy"
        ).copy(
            meta = Meta(is_end = true, 50, 1000)
        )

        Mockito.`when`(
            kakaoRepository.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(Result.Success(mockItem))

        searchViewModel.inputSearchEditText.value = "안드로이드"

        searchViewModel.search()

        delay(100L)

        Mockito.verify(viewStateObserver)
            .onChanged(SearchViewModel.SearchViewState.PageEnd)

    }


    @Test
    fun checkInputErrorTest() = runBlocking {
        searchViewModel.inputSearchEditText.value = null

        searchViewModel.search()

        delay(100L)

        Mockito.verify(viewStateObserver)
            .onChanged(SearchViewModel.SearchViewState.EmptyInput)
    }

    @Test
    fun checkSearchErrorTest() = runBlocking {
        val mockException = Exception("Api Error")
        Mockito.`when`(
            kakaoRepository.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(Result.Error(mockException))

        searchViewModel.inputSearchEditText.value = "안드로이드"

        searchViewModel.search()

        delay(100L)

        Mockito.verify(viewStateObserver)
            .onChanged(SearchViewModel.SearchViewState.Error(message = "Api Error"))

    }
}