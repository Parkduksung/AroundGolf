package com.example.aroundgolf.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GolfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerGolfEntity(entity: GolfEntity): Long

    @Query("SELECT * FROM golf_table")
    fun getAll(): List<GolfEntity>

    @Query("SELECT * FROM golf_table WHERE `like` = (:like)")
    fun getBookmarkGolfEntity(like: Boolean = true): List<GolfEntity>

    @Query("SELECT * FROM golf_table WHERE `name` = (:name)")
    fun getGolfEntity(name: String): GolfEntity

    @Query("SELECT * FROM golf_table WHERE `name` = (:name) ")
    fun isExistGolfEntity(name: String): Long

    @Query("SELECT * FROM golf_table WHERE `name` = (:name) AND `like` = (:like) ")
    fun isExistBookmarkGolfEntity(
        name: String,
        like: Boolean = true
    ): Long

    @Query("UPDATE golf_table SET `like` = (:like) WHERE  name = (:name) AND address = (:address)")
    fun updateBookmarkGolfEntity(
        name: String,
        address: String,
        like: Boolean
    ): Int
}