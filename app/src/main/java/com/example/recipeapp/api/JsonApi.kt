package com.example.recipeapp.api

import com.example.recipeapp.data.ExampleItem
import retrofit2.Call
import retrofit2.http.GET

interface JsonApi {

    @GET("api/")
    fun getresults(): Call<ExampleItem>

}