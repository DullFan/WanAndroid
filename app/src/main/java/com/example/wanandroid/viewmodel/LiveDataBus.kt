package com.example.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData

object LiveDataBus {
    private val bus : MutableMap<String, MutableLiveData<Any>> by lazy { HashMap() }

    //添加同步锁
    @Synchronized
    fun <T> with(key:String,type : Class<T>) : MutableLiveData<T>{
        //判断是否包含这个key，防止重复key
        if(!bus.containsKey(key)){
            bus[key] = MutableLiveData()
        }
        return bus[key] as MutableLiveData<T>
    }
}