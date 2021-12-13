package com.example.aroundgolf.data.source.local

import com.example.aroundgolf.room.GolfEntity
import com.example.aroundgolf.utils.Result

interface GolfLocalDataSource {

    suspend fun registerBookmarkGolf(golfEntity: GolfEntity): Boolean

    suspend fun toggleBookmarkGolf(
        item: GolfEntity
    ): Result<GolfEntity>

    suspend fun getAllBookmarkList(): Result<List<GolfEntity>>

    suspend fun getAll(): Result<List<GolfEntity>>

    suspend fun getGolfEntity(
        name: String
    ): Result<GolfEntity>

    suspend fun isExistGolfEntity(
        name: String
    ): Boolean

    suspend fun isExistBookmarkGolfEntity(
        name: String
    ): Boolean

    suspend fun registerAll(
        list: List<GolfEntity>
    ): Boolean

}