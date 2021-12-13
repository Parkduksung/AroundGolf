package com.example.aroundgym.data.repo

import base.BaseTest
import com.example.aroundgym.data.source.KAKAORemoteDataSource
import com.example.aroundgym.di.ApiModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import com.example.aroundgym.util.Result

class KAKAORepositoryImplTest : BaseTest() {

    @Mock
    lateinit var kakaoRemoteDataSource: KAKAORemoteDataSource

    private lateinit var kakaoRepositoryImpl: KAKAORepositoryImpl

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        kakaoRemoteDataSource = Mockito.mock(KAKAORemoteDataSource::class.java)
        kakaoRepositoryImpl = KAKAORepositoryImpl(kakaoRemoteDataSource)
    }


    @Test
    fun searchBookSuccessTest() = runBlocking {

        val mockResponse = ApiModule.provideKakaoApi().getSearchBook(
            query = "안드로이드",
            size = 50,
            page = 1,
            sort = "accuracy"
        )

        Mockito.`when`(
            kakaoRemoteDataSource.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(Result.Success(mockResponse))

        MatcherAssert.assertThat(
            "올바른 KakaoSearchBookResponse 값이 나오므로 성공",
            ((kakaoRepositoryImpl.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )) as Result.Success).data.documents,
            Matchers.`is`(mockResponse.documents)
        )
    }


    @Test
    fun searchBookFailTest() = runBlocking {

        val mockResponse = Exception("Api Error")

        Mockito.`when`(
            kakaoRemoteDataSource.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(Result.Error(mockResponse))

        MatcherAssert.assertThat(
            "Error 값이 나오므로 실패",
            ((kakaoRepositoryImpl.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )) as Result.Error).exception.message,
            Matchers.`is`(mockResponse.message)
        )
    }
}