package com.example.wanandroid.api

import com.example.wanandroid.entity.*
import retrofit2.http.*

interface WanAndroidApi {

    //登录,表单的形式提交
    @POST("/user/login")
    @FormUrlEncoded
    suspend fun loginAction(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginBean

    //注册
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun registerAction(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): LoginBean

    //获取个人信息
    @GET("/user/lg/userinfo/json")
    suspend fun informationAction(
        @Header("Cookie") username: String,
        @Header("Cookie") password: String
    ): InformationBean


    //获取积分排行
    @GET()
    suspend fun rankingAction(
       @Url url:String
    ): RankingBean

    //获取积分详情
    @GET()
    suspend fun integralAction(
        @Header("Cookie") username: String,
        @Header("Cookie") password: String,
        @Url url:String
    ): IntegralBean

    //获取首页Banner
    @GET("/banner/json")
    suspend fun bannerAction(): BannerBean


    //获取首页文章
    @GET()
    suspend fun homeArticlesAction(@Url url:String): HomeArticlesBean
}