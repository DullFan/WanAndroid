package com.example.wanandroid.adapter

import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragmentActivity: FragmentActivity, private val dataList:List<Fragment>)
    :FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int = dataList.size

    override fun createFragment(position: Int): Fragment = dataList[position]

}
