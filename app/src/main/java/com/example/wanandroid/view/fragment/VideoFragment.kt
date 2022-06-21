package com.example.wanandroid.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.wanandroid.R
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.constant.videoList
import com.example.wanandroid.databinding.FragmentVideoBinding
import com.example.wanandroid.view.ui.LoadingView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VideoFragment(private val url: String, private val vp: ViewPager2) : Fragment() {
    private val TAG = "VideoFragment"
    private val mediaPlayer = MediaPlayer()
    private lateinit var loadingView: LoadingView
    private lateinit var binding: FragmentVideoBinding
    private var flag = true
    //用来控制一开始监听视频播放会多出几个的问题
    private var flag2 = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        //显示加载框
        context?.let {
            loadingView = LoadingView(it, R.style.CustomDialog)
            loadingView.show()
        }

        //设置播放地址
        mediaPlayer.apply {
            setOnPreparedListener {
                //视频准备完成,开始播放
                loadingView.dismiss()
                binding.itemVideoSeek.max = mediaPlayer.duration
                flag2 = true
                it.start()
            }
            setDataSource(url)
            prepareAsync()
            //视频播放结束
            setOnCompletionListener {
                if(flag2){
                    if (vp.currentItem + 1 == videoList.size - 1) {
                        vp.currentItem = 1
                    }else{
                        vp.currentItem = vp.currentItem + 1
                    }
                }
            }
        }

        binding.itemVideoSurface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {}

            //开始改变的时候调用
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                //设置显示的View
                mediaPlayer.setDisplay(holder)
                //播放时常亮
                mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })

        //每隔0.5秒监听视频当前播放时长
        lifecycleScope.launch {
            while (true) {
                if(flag){
                    binding.itemVideoSeek.progress = mediaPlayer.currentPosition
                }
                delay(500)
            }
        }

        //拖动进度条时关闭监听
        binding.itemVideoSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.itemVideoText.text = "${progress}/${mediaPlayer.duration}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                flag = false
                binding.itemVideoText.visibility = View.VISIBLE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                flag = true
                mediaPlayer.seekTo(seekBar!!.progress)
                binding.itemVideoText.visibility = View.GONE
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        lifecycleScope.launch {
            while (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                delay(500)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        lifecycleScope.launch {
            while (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                delay(500)
            }
        }
    }
}