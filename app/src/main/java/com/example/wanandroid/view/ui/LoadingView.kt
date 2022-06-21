package com.example.wanandroid.view.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.example.wanandroid.R


class LoadingView(context: Context, theme:Int) : Dialog(context,theme){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(context)
    }

    private fun initView(context: Context) {
        //当设置为false，点击返回键是无法返回的。默认为true
        setCancelable(false)
        //当设置为false,点击对话框以外的区域时Dialog不消失,默认为true
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_loading_layout)
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }
}