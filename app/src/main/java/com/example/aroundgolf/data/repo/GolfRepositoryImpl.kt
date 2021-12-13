package com.example.aroundgolf.data.repo

import com.example.aroundgolf.api.response.GolfResponse
import com.example.aroundgolf.data.source.local.GolfLocalDataSource
import com.example.aroundgolf.data.source.remote.GolfRemoteDataSource
import com.example.aroundgolf.room.GolfEntity
import com.example.aroundgolf.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class GolfRepositoryImpl :
    GolfRepository {

    private val golfRemoteDataSource by inject<GolfRemoteDataSource>(
        GolfRemoteDataSource::class.java
    )

    private val golfLocalDataSource by inject<GolfLocalDataSource>(
        GolfLocalDataSource::class.java
    )

    override fun getGolfList(callback: (Result<GolfResponse>) -> Unit) {
        golfRemoteDataSource.getGolfList(callback)
    }

    override suspend fun registerBookmarkGolf(golfEntity: GolfEntity): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.registerBookmarkGolf(golfEntity)
        }

    override suspend fun toggleBookmarkGolf(item: GolfEntity): Result<GolfEntity> =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.toggleBookmarkGolf(item)
        }

    override suspend fun getAllBookmarkList(): Result<List<GolfEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.getAllBookmarkList()
        }

    override suspend fun getGolfEntity(name: String): Result<GolfEntity> =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.getGolfEntity(name)
        }

    override suspend fun isExistBookmarkGolfEntity(name: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.isExistBookmarkGolfEntity(name)
        }

    override suspend fun isExistGolfEntity(name: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.isExistGolfEntity(name)
        }

    override suspend fun getAll(): Result<List<GolfEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.getAll()
        }

    override suspend fun registerAll(list: List<GolfEntity>): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfLocalDataSource.registerAll(list)
        }
}