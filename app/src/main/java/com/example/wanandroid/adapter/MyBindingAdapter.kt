package com.example.wanandroid.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide

object MyBindingAdapter {

    @JvmStatic
    @androidx.databinding.BindingAdapter("glide")
    fun glide(imageView: ImageView,url:String){
        Glide.with(imageView.context).load(url).into(imageView)
    }

}