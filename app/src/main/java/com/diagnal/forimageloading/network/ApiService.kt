package com.diagnal.forimageloading.network

import com.diagnal.forimageloading.data.ForImageDTOItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: Client-ID "+ ApiUtils.API_KEY)
    @GET("photos")
    fun getPhoto(@Query("page") page: Int,
                 @Query("per_page") perPage: Int): Call<List<ForImageDTOItem>> // Define your model class for Post
}