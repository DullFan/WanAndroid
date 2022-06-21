package com.example.wanandroid.entity

data class IntegralBean(
    val `data`: IntegralData,
    val errorCode: Int,
    val errorMsg: String
)

data class IntegralData(
    val curPage: Int,
    val datas: List<IntegralDataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class IntegralDataX(
    val coinCount: String,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)