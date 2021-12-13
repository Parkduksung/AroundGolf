package com.example.aroundgym.data.source

import base.BaseTest
import com.example.aroundgym.api.KakaoApi
import com.example.aroundgym.di.ApiModule
import com.example.aroundgym.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class KAKAORemoteDataSourceImplTest : BaseTest() {

    @Mock
    lateinit var kakaoApi: KakaoApi

    private lateinit var kakaoRemoteDataSourceImpl: KAKAORemoteDataSourceImpl

    @ExperimentalCoroutinesApi
    override fun setup() {
        super.setup()
        kakaoApi = Mockito.mock(KakaoApi::class.java)
        kakaoRemoteDataSourceImpl = KAKAORemoteDataSourceImpl(kakaoApi)
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
            kakaoApi.getSearchBook(
                query = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            )
        ).thenReturn(
            mockResponse
        )

        MatcherAssert.assertThat(
            "올바른 KakaoSearchBookResponse 값이 나오므로 성공",
            (kakaoRemoteDataSourceImpl.searchBook(
                bookName = "안드로이드",
                size = 50,
                page = 1,
                sort = "accuracy"
            ) as Result.Success).data,
            Matchers.`is`(mockResponse)
        )
    }

}