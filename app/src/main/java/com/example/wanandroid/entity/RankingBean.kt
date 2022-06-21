package com.example.wanandroid.entity

import retrofit2.http.GET

data class RankingBean(
    val `data`: Leaderboard,
    val errorCode: Int,
    val errorMsg: String
)

data class Leaderboard(
    val curPage: Int,
    val datas: List<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class DataX(
    val coinCount: String,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)