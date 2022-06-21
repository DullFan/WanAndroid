package com.example.wanandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanandroid.entity.PhotoBean
import com.example.wanandroid.repository.PixabayApiRepository
import kotlinx.coroutines.launch

class PhotoViewModel :ViewModel(){
    var photoLiveData = MutableLiveData<PhotoBean>()

    fun requestPhoto(type:String,page:Int){
        viewModelScope.launch {
            photoLiveData.value = PixabayApiRepository().requestPhoto(type,page)
        }
    }
}