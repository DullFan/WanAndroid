package com.example.wanandroid.view.activity.video

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyMediaPlayer :MediaPlayer(),LifecycleObserver{
    var index:Int = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer(){
        pause()
        //获取当前播放位置
        index = currentPosition
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer(){
        start()
        //继续播放
        seekTo(index)
    }

}