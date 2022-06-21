package com.example.wanandroid.repository

import android.util.Log
import com.example.wanandroid.api.WanAndroidApi
import com.example.wanandroid.api.WanAndroidApiClient
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.entity.*
import com.example.wanandroid.viewmodel.LiveDataBus

class WanAndroidApiRepository {
    //登录
    suspend fun requestLogin(userName: String, password: String): LoginBean {
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .loginAction(userName, password)
    }

    //注册
    suspend fun requestRegister(userName: String, password: String, rePassword: String): LoginBean {
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .registerAction(userName, password, rePassword)
    }

    //获取个人信息
    suspend fun requestInformation(): InformationBean {
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .informationAction(
                "loginUserName=${
                    LiveDataBus.with(
                        Constant.USER_NANE,
                        String::class.java
                    ).value
                }", "loginUserPassword=${
                    LiveDataBus.with(
                        Constant.PASSWORD,
                        String::class.java
                    ).value
                }"
            )
    }

    //获取积分排行
    suspend fun requestLeaderboard(url: String): RankingBean {
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .rankingAction(url)
    }

    //获取积分明细
    suspend fun requestIntegral(url: String): IntegralBean {
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .integralAction(
                "loginUserName=${
                    LiveDataBus.with(
                        Constant.USER_NANE,
                        String::class.java
                    ).value
                }", "loginUserPassword=${
                    LiveDataBus.with(
                        Constant.PASSWORD,
                        String::class.java
                    ).value
                }",
                url
            )
    }

    //获取首页Banner
    suspend fun requestBanner(): BannerBean{
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .bannerAction()
    }

    //获取首页Banner
    suspend fun requestHomeArticles(url:String): HomeArticlesBean{
        return WanAndroidApiClient.WanAndroidApiClient
            .wanAndroidApiRetrofit(WanAndroidApi::class.java)
            .homeArticlesAction(url)
    }
}