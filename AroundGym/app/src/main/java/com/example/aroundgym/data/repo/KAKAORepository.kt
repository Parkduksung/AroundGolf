package com.example.aroundgym.data.repo

import com.example.aroundgym.api.response.KakaoSearchBookResponse
import com.example.aroundgym.util.Result

interface KAKAORepository {

    suspend fun searchBook(
        bookName: String,
        size: Int,
        sort: String,
        page: Int
    ) : Result<KakaoSearchBookResponse>
}