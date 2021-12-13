package com.example.aroundgym.api

import com.example.aroundgym.api.response.KakaoSearchBookResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoApi {


    @Headers("Authorization: KakaoAK $REST_API_KEY")
    @GET(SEARCH_BOOK)
    suspend fun getSearchBook(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): KakaoSearchBookResponse

    companion object {
        private const val BASE_KAKAO_URL = "https://dapi.kakao.com/"
        private const val REST_API_KEY = "7f0bc613532236da7fe88cf3b1bc3160"
        private const val SEARCH_BOOK = "v3/search/book"


        fun create(): KakaoApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_KAKAO_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KakaoApi::class.java)
        }
    }

}