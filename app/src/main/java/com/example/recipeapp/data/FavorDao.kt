package com.example.recipeapp.data

import androidx.room.*

@Dao
interface FavorDao {

    //Só preciso da lista completa
    @Query("SELECT * FROM recip_table")
    fun getFavourites(): List<Results>

    @Query("SELECT * FROM recip_table WHERE title LIKE :name")
    fun findRecipe(name: String): Results

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rec: Results)

    @Delete
    fun delete(rec: Results)
}