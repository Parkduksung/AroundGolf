package com.example.aroundgolf.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GolfEntity::class], version = 3)
abstract class GolfDatabase : RoomDatabase(){

    abstract fun campingDao(): GolfDao
}