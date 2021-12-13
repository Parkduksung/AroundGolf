package com.example.aroundgym.data.source

import com.example.aroundgym.api.response.KakaoSearchBookResponse
import com.example.aroundgym.util.Result

interface KAKAORemoteDataSource {

    suspend fun searchBook(
        bookName: String,
        size: Int,
        sort: String,
        page: Int
    ) : Result<KakaoSearchBookResponse>
}