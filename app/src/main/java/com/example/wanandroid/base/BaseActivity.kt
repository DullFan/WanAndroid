package com.example.wanandroid.base

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wanandroid.R
import com.example.wanandroid.constant.Constant.WEB_VIEW_URL
import com.example.wanandroid.view.activity.search.SearchActivity

open class BaseActivity : AppCompatActivity() {
    val toolbar: Toolbar by lazy {
        findViewById(R.id.layout_title_toolbar)
    }

    override fun onResume() {
        super.onResume()
        setSupportActionBar(toolbar)
        //判断是否有传递参数，有的话显示返回图标，没有标题则显示列表图标
        intent.getStringExtra("title")?.let {
            supportActionBar?.title = it
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } ?: toolbar.setNavigationIcon(R.drawable.title_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //判断标题是否为空，
                intent.getStringExtra("title")?.let {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //显示Toast
    fun showToast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }

    fun showLog(content: String) {
        Log.i("特殊的Log", content)
    }

    //跳转
    fun <T> myStartActivity(clazz: Class<T>, title: String) {
        Intent(this, clazz).apply {
            putExtra("title", title)
            startActivity(this)
        }
    }

    //跳转
    fun <T> myStartActivity(clazz: Class<T>, title: String,url: String) {
        Intent(this, clazz).apply {
            putExtra("title", title)
            putExtra(WEB_VIEW_URL, url)
            startActivity(this)
        }
    }

    //跳转
    fun <T> myStartActivity(clazz: Class<T>) {
        Intent(this, clazz).apply {
            startActivity(this)
        }
    }

    //获取ViewModel
    inline fun <T : ViewModel> getViewModel(clazz: Class<T>) =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(clazz)
}