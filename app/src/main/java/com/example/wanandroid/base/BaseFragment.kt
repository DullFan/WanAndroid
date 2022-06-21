package com.example.wanandroid.base

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wanandroid.R
import com.example.wanandroid.constant.Constant

open class BaseFragment : Fragment() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //判断标题是否为空，
                activity?.intent?.getStringExtra("title")?.let {
                    activity?.finish()
                } ?: showToast("点击了列表")
            }
            R.id.menu_search -> showToast("点击了搜索")
        }
        return super.onOptionsItemSelected(item)
    }

    //显示Toast
    fun showToast(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show()
    }

    //跳转
    fun <T> myStartActivity(clazz: Class<T>, title: String) {
        Intent(context, clazz).apply {
            putExtra("title", title)
            startActivity(this)
        }
    }

    //跳转
    fun <T> myStartActivity(clazz: Class<T>, title: String,url: String) {
        Intent(context, clazz).apply {
            putExtra("title", title)
            putExtra(Constant.WEB_VIEW_URL, url)
            startActivity(this)
        }
    }

    fun showLog(content: String) {
        Log.i("特殊的Log", content)
    }

    //获取ViewModel
    inline fun <T : ViewModel> getViewModel(clazz: Class<T>) =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(clazz)
}