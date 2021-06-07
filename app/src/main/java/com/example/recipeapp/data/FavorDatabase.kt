package com.example.recipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Results::class], version = 1)
abstract class FavorDatabase: RoomDatabase() {
    abstract fun favorDao(): FavorDao
}