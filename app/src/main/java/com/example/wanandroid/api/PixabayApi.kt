package com.example.wanandroid.api

import com.example.wanandroid.entity.PhotoBean
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("?key=25087437-ea61aa8180cfa3b2471a4112a")
    suspend fun photoAction(@Query("q")type:String
                            ,@Query("page")page:Int): PhotoBean
}