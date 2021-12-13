package com.example.aroundgym.data.source

import com.example.aroundgym.api.KakaoApi
import com.example.aroundgym.api.response.KakaoSearchBookResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.aroundgym.util.Result


class KAKAORemoteDataSourceImpl @Inject constructor(private val kakaoApi: KakaoApi) :
    KAKAORemoteDataSource {


    override suspend fun searchBook(
        bookName: String,
        size: Int,
        sort: String,
        page: Int
    ): Result<KakaoSearchBookResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            Result.Success(
                kakaoApi.getSearchBook(
                    query = bookName,
                    size = size,
                    sort = sort,
                    page = page
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}