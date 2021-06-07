package com.example.recipeapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recip_table")
data class Results(
    @PrimaryKey var title: String,
    @ColumnInfo(name = "href")var href: String,
    @ColumnInfo(name = "ingredients")var ingredients: String,
    @ColumnInfo(name = "thumbnail")var thumbnail: String
)
