package com.example.wanandroid.view.activity.video

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.videoList
import com.example.wanandroid.databinding.ActivityVideoBinding
import com.example.wanandroid.view.fragment.VideoFragment


class VideoActivity : BaseActivity() {

    private lateinit var binding: ActivityVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoViewpager2.apply {
            adapter = object : FragmentStateAdapter(this@VideoActivity) {
                override fun getItemCount(): Int = videoList.size

                override fun createFragment(position: Int): Fragment =
                    VideoFragment(videoList[position], binding.videoViewpager2)
            }
        }
    }
}