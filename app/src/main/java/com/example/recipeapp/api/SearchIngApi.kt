package com.example.recipeapp.api

import com.example.recipeapp.data.ExampleItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchIngApi {

    //Função de pesquisa
    //Usado checkbox para pesquisar por ingredientes
    @GET("api")
    fun getsearchbyingredients(@Query("i") i: String):
            Call<ExampleItem>

}