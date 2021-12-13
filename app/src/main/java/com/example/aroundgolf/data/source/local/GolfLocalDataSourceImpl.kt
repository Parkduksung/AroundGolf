package com.example.aroundgolf.data.source.local

import com.example.aroundgolf.room.GolfDatabase
import com.example.aroundgolf.room.GolfEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import com.example.aroundgolf.utils.Result
import kotlinx.coroutines.delay

class GolfLocalDataSourceImpl : GolfLocalDataSource {

    private val golfDatabase by inject<GolfDatabase>(GolfDatabase::class.java)

    override suspend fun registerBookmarkGolf(golfEntity: GolfEntity): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfDatabase.campingDao()
                .registerGolfEntity(golfEntity) > 0
        }

    override suspend fun toggleBookmarkGolf(item: GolfEntity): Result<GolfEntity> =
        withContext(Dispatchers.IO) {
            val updateCampingData = golfDatabase.campingDao().updateBookmarkGolfEntity(
                name = item.name,
                address = item.address,
                like = !item.like
            )
            return@withContext if (updateCampingData == 1) {
                val updateVocaEntity = item.copy(like = !item.like)
                Result.Success(updateVocaEntity)
            } else {
                Result.Error(Exception(Throwable("Error ToggleBookmark")))
            }
        }

    override suspend fun getGolfEntity(name: String): Result<GolfEntity> =
        withContext(Dispatchers.IO)
        {
            return@withContext try {
                if (isExistGolfEntity(name)) {
                    Result.Success(golfDatabase.campingDao().getGolfEntity(name))
                } else {
                    Result.Error(Exception(Throwable("Not Exist Data")))
                }
            } catch (e: Exception) {
                Result.Error(Exception(Throwable("GetCampingData Error!")))
            }
        }

    override suspend fun isExistBookmarkGolfEntity(name: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfDatabase.campingDao().isExistBookmarkGolfEntity(name) > 0L
        }

    override suspend fun isExistGolfEntity(name: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext golfDatabase.campingDao().isExistGolfEntity(name) > 0L
        }

    override suspend fun getAllBookmarkList(): Result<List<GolfEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val getAllBookmarkList =
                    golfDatabase.campingDao().getBookmarkGolfEntity(true)
                Result.Success(getAllBookmarkList)
            } catch (e: Exception) {
                Result.Error(Exception(Throwable("bookmarkList is Null!")))
            }
        }

    override suspend fun getAll(): Result<List<GolfEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val getAllBookmarkList =
                    golfDatabase.campingDao().getAll()
                Result.Success(getAllBookmarkList)
            } catch (e: Exception) {
                Result.Error(Exception(Throwable("bookmarkList is Null!")))
            }
        }


    override suspend fun registerAll(list: List<GolfEntity>): Boolean =
        withContext(Dispatchers.IO) {
            list.forEach {
                registerBookmarkGolf(it)
            }
            delay(1000L)
            return@withContext golfDatabase.campingDao().getAll().size == list.size
        }

}