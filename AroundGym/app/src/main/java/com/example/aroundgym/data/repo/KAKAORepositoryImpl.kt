package com.example.aroundgym.data.repo


import com.example.aroundgym.api.response.KakaoSearchBookResponse
import com.example.aroundgym.data.source.KAKAORemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.aroundgym.util.Result

class KAKAORepositoryImpl @Inject constructor(private val kakaoRemoteDataSource: KAKAORemoteDataSource) :
    KAKAORepository {

    override suspend fun searchBook(
        bookName: String,
        size: Int,
        sort: String,
        page: Int
    ): Result<KakaoSearchBookResponse> = withContext(Dispatchers.IO) {
        return@withContext kakaoRemoteDataSource.searchBook(bookName, size, sort, page)
    }
}