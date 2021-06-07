package com.example.recipeapp.api

import com.example.recipeapp.data.ExampleItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchNameApi {

    //Função de pesquisa
    //Usado checkbox para pesquisar por nome do prato
    @GET("api")
    fun getsearchbyname(@Query("q") q : String):
            Call<ExampleItem>

}