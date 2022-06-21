package com.example.wanandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanandroid.entity.*
import com.example.wanandroid.repository.WanAndroidApiRepository
import kotlinx.coroutines.launch

class WanAndroidViewModel : ViewModel() {
    /**
     * 登录
     */
    var loginBean = MutableLiveData<LoginBean>()

    /**
     * 注册
     */
    var registerBean = MutableLiveData<LoginBean>()

    /**
     * 获取个人信息
     */
    var informationBean = MutableLiveData<InformationBean>()

    /**
     * 获取积分排行
     */
    var leaderboardBean = MutableLiveData<RankingBean>()

    /**
     * 获取积分明细
     */
    var integralBean = MutableLiveData<IntegralBean>()

    /**
     * 获取首页Banner
     */
    var bannerBean = MutableLiveData<BannerBean>()

    var homeArticlesBean = MutableLiveData<HomeArticlesBean>()

    fun requestLogin(userName: String, password: String) {
        viewModelScope.launch {
            loginBean.value = WanAndroidApiRepository().requestLogin(userName, password)
        }
    }

    fun requestRegister(userName: String, password: String,rePassword: String) {
        viewModelScope.launch {
            registerBean.value = WanAndroidApiRepository().requestRegister(userName, password,rePassword)
        }
    }

    fun requestInformation(userName: String, password: String) {
        viewModelScope.launch {
            informationBean.value = WanAndroidApiRepository().requestInformation()
        }
    }

    fun requestLeaderboard(url:String){
        viewModelScope.launch {
            leaderboardBean.value = WanAndroidApiRepository().requestLeaderboard(url)
        }
    }


    fun requestIntegral(url:String){
        viewModelScope.launch {
            integralBean.value = WanAndroidApiRepository().requestIntegral(url)
        }
    }

    fun requestBanner(){
        viewModelScope.launch {
            bannerBean.value = WanAndroidApiRepository().requestBanner()
        }
    }

    fun requestHomeArticles(url:String){
        viewModelScope.launch {
            homeArticlesBean.value = WanAndroidApiRepository().requestHomeArticles(url)
        }
    }
}