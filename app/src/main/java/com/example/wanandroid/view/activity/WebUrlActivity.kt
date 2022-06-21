package com.example.wanandroid.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.wanandroid.R
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.databinding.ActivityWebUrlBinding

class WebUrlActivity : BaseActivity() {
    private lateinit var binding: ActivityWebUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebUrlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra(Constant.WEB_VIEW_URL)
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.apply {
                //支持JS
                javaScriptEnabled = true
                //支持自动加载图片
                loadsImagesAutomatically = true
                //自适应屏幕
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                loadWithOverviewMode = true
                //扩大比例的缩放
                useWideViewPort = true
                //设置出现缩放工具
                builtInZoomControls = true
                //设置可以支持缩放
                setSupportZoom(true)
                setWebChromeClient(object :WebChromeClient(){
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if(newProgress == 100){
                            binding.webProgressBar.visibility = View.GONE
                        }else{
                            binding.webProgressBar.visibility = View.VISIBLE
                            binding.webProgressBar.progress = newProgress
                        }
                    }
                })
            }


            if (url != null) {
                loadUrl(url)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(binding.webView.canGoBack()){
                    binding.webView.goBack()
                }else{
                    finish()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
            return
        }
        super.onBackPressed()
    }
}