package com.example.wanandroid.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WanAndroidApiClient {
    companion object{
        val WanAndroidApiClient = WanAndroidApiClient()
    }

    fun <T> wanAndroidApiRetrofit(apiClass:Class<T>) : T{
        val httpLoggingInterceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger(){
                Log.i("WanAndroidApiClient", it)
            })

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val build = OkHttpClient().newBuilder().apply {
            //读取超时时间
            readTimeout(50000, TimeUnit.SECONDS)
            //连接超时时间
            connectTimeout(50000, TimeUnit.SECONDS)
            //写出超时时间
            writeTimeout(50000, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }.build()

        var retrofit = Retrofit.Builder().baseUrl("https://www.wanandroid.com/")
            .client(build)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(apiClass)
    }
}