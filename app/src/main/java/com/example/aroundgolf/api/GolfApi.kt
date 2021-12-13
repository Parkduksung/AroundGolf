package com.example.aroundgolf.api

import com.example.aroundgolf.api.response.GolfResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GolfApi {
    companion object {
        private const val BASE_URL = "GolfPlace"

        private const val GOLF_API_KEY = "12831f56963a47eab0af0053f0cc5ae6"
        private const val DEFAULT_TYPE = "json"
        private const val DEFAULT_SIZE = 177
    }

    @GET(BASE_URL)
    fun getGolfResponse(
        @Query(value = "type") type: String = DEFAULT_TYPE,
        @Query(value = "KEY") key: String = GOLF_API_KEY,
        @Query(value = "pSize") pSize: Int = DEFAULT_SIZE
    ): Call<GolfResponse>

}