package com.example.wanandroid.repository

import android.util.Log
import com.example.wanandroid.api.PixabayApi
import com.example.wanandroid.api.PixabayApiClient
import com.example.wanandroid.entity.PhotoBean

//仓库
class PixabayApiRepository {
    suspend fun requestPhoto(type:String,page:Int):PhotoBean{
        return PixabayApiClient.pixabayApiClient.pixabayApiRetrofit(PixabayApi::class.java)
            .photoAction(type,page)
    }
}

