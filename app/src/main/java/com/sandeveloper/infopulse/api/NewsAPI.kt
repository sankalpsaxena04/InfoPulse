package com.sandeveloper.infopulse.api

import com.sandeveloper.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("top-headlines")
    suspend fun news( @Query("category") cat:String,@Query("country") country:String, @Query("apiKey") key:String): Response<NewsResponse>
}