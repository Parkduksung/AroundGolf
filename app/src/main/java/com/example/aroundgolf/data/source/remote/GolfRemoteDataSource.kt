package com.example.aroundgolf.data.source.remote

import com.example.aroundgolf.api.response.GolfResponse
import com.example.aroundgolf.utils.Result

interface GolfRemoteDataSource {

    fun getGolfList(
        callback: (Result<GolfResponse>) -> Unit
    )

}