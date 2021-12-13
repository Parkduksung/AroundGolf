package com.example.aroundgolf.data.source.remote


import com.example.aroundgolf.api.GolfApi
import com.example.aroundgolf.api.response.GolfResponse
import com.example.aroundgolf.utils.Result
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GolfRemoteDataSourceImpl :
    GolfRemoteDataSource {

    private val golfApi by inject<GolfApi>(GolfApi::class.java)

    override fun getGolfList(callback: (Result<GolfResponse>) -> Unit) {
        golfApi.getGolfResponse().enqueue(object : Callback<GolfResponse> {
            override fun onResponse(
                call: Call<GolfResponse>,
                response: Response<GolfResponse>
            ) {
                response.body()?.let { callback(Result.Success(it)) }
            }

            override fun onFailure(call: Call<GolfResponse>, t: Throwable) {
                callback(Result.Error(Exception(t)))
            }
        })
    }
}