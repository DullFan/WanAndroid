package com.example.wanandroid.view.activity

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseRvAdapter
import com.example.wanandroid.adapter.MainViewPagerAdapter
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant.PASSWORD
import com.example.wanandroid.constant.Constant.SHARED_PREFERENCES_NAME
import com.example.wanandroid.constant.Constant.USER_INTEGRAL
import com.example.wanandroid.constant.Constant.USER_NANE
import com.example.wanandroid.databinding.ActivityMainBinding
import com.example.wanandroid.databinding.ItemHeadLayoutBinding
import com.example.wanandroid.entity.HeadItemBean
import com.example.wanandroid.view.activity.integral.Integral
import com.example.wanandroid.view.activity.leaderboard.Leaderboard
import com.example.wanandroid.view.activity.login.Login
import com.example.wanandroid.view.activity.photo.PhotoActivity
import com.example.wanandroid.view.activity.search.SearchActivity
import com.example.wanandroid.view.activity.video.VideoActivity
import com.example.wanandroid.view.fragment.*
import com.example.wanandroid.viewmodel.LiveDataBus
import com.example.wanandroid.viewmodel.WanAndroidViewModel


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    val headItemBeanList = mutableListOf<HeadItemBean>()
    private lateinit var viewModel: WanAndroidViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        initUser()
        title = "??????"
        initBottomNav()
        initSide()
        initLogin()
        initIntegral()
    }

    /**
     * ????????????????????????
     */
    private fun initIntegral() {
        binding.drawerHeaderSignal.setOnClickListener {
            myStartActivity(Leaderboard::class.java, "????????????")
        }
    }

    /**
     * ???????????????????????????
     */
    private fun initDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("????????????")
            setPositiveButton("??????") { dialog, sss ->
                myStartActivity(Login::class.java, "??????")
            }
            show()
        }

    }

    /**
     * ???????????????????????????
     */
    private fun initUser() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        LiveDataBus.with(USER_NANE, String::class.java).value =
            sharedPreferences.getString(USER_NANE, null)
        LiveDataBus.with(PASSWORD, String::class.java).value =
            sharedPreferences.getString(PASSWORD, null)
        showLog("ViewModel???:??????${LiveDataBus.with(USER_NANE, String::class.java).value}")
        showLog("ViewModel???:??????${LiveDataBus.with(PASSWORD, String::class.java).value}")
    }

    /**
     * ????????????????????????
     */
    private fun initLogin() {
        binding.drawerHeaderName.setOnClickListener {
            if (LiveDataBus.with(USER_NANE, String::class.java).value == null) {
                myStartActivity(Login::class.java, "??????")
            }
        }
        if (LiveDataBus.with(USER_NANE, String::class.java).value != null) {
            binding.drawerHeaderName.text = LiveDataBus.with(USER_NANE, String::class.java).value
            viewModel.requestInformation(
                "${LiveDataBus.with(USER_NANE, String::class.java).value}",
                "${LiveDataBus.with(PASSWORD, String::class.java).value}"
            )
            viewModel.informationBean.observe(this) {
                binding.drawerHeaderRanking.text =
                    "??????:${it.data.coinInfo.level} ??????:${it.data.coinInfo.rank}"
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.mainDrawerLayout.open()
            }
            R.id.menu_search -> {
                myStartActivity(SearchActivity::class.java, "??????")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * ???????????????
     */
    private fun initSide() {
        headItemBeanList.clear()
        //????????????
        binding.mainDrawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val fl = 1 - slideOffset / 12
                binding.mainCv.scaleX = fl
                binding.mainCv.scaleY = fl
                val fl2 = slideOffset * 20 * resources.displayMetrics.density + 0.5f
                binding.mainCv.radius = fl2
            }
        })

//        //????????????????????????
//        binding.mainNavigation.layoutParams.apply {
//            width = resources.displayMetrics.widthPixels
//            binding.mainNavigation.layoutParams = this
//        }

        //?????????????????????Rv
        headItemBeanList += HeadItemBean(R.drawable.menu_integral_img, "????????????")
        headItemBeanList += HeadItemBean(R.drawable.menu_like_img, "????????????")
        headItemBeanList += HeadItemBean(R.drawable.menu_todo_img, "TODO")
        headItemBeanList += HeadItemBean(R.drawable.menu_settings_img, "????????????")
        headItemBeanList += HeadItemBean(R.drawable.menu_insert_photo_img, "??????")
        headItemBeanList += HeadItemBean(R.drawable.menu_video_img, "??????")
        if (LiveDataBus.with(USER_NANE, String::class.java).value != null) {
            headItemBeanList += HeadItemBean(R.drawable.menu_sign_out_img, "????????????")
        }

        binding.drawerHeaderRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter =
                object : BaseRvAdapter<HeadItemBean>(R.layout.item_head_layout, headItemBeanList) {
                    override fun onBind(
                        rvBinding: ViewDataBinding,
                        data: HeadItemBean,
                        position: Int
                    ) {
                        rvBinding as ItemHeadLayoutBinding
                        rvBinding.itemHeadImg.setImageResource(data.img)
                        rvBinding.itemHeadText.text = data.headName
                        if (position == 0) {
                            rvBinding.itemHeadIntegral.visibility = View.VISIBLE
                            if (LiveDataBus.with(USER_NANE, String::class.java).value == null) {
                                rvBinding.itemHeadIntegral.text = ""
                            } else {
                                viewModel.informationBean.observe(this@MainActivity) {
                                    rvBinding.itemHeadIntegral.text =
                                        "${it.data.coinInfo.coinCount}"
                                }
                            }
                        } else {
                            rvBinding.itemHeadIntegral.visibility = View.GONE
                        }
                        //????????????
                        rvBinding.root.setOnClickListener {
                            when (data.headName) {
                                "????????????" -> {
                                    if (LiveDataBus.with(
                                            USER_NANE,
                                            String::class.java
                                        ).value != null
                                    ) {
                                        try {
                                            LiveDataBus.with(
                                                USER_INTEGRAL,
                                                String::class.java
                                            ).value =
                                                viewModel.informationBean.value!!.data.coinInfo.coinCount.toString()
                                        } catch (e: Exception) {
                                            showToast("????????????????????????")
                                        }
                                        myStartActivity(Integral::class.java, "????????????")
                                    } else {
                                        initDialog()
                                    }
                                }
                                "????????????" -> ""
                                "TODO" -> ""
                                "????????????" -> ""
                                "??????" -> myStartActivity(PhotoActivity::class.java, data.headName)
                                "??????" -> myStartActivity(VideoActivity::class.java, data.headName)
                                "????????????" -> {
                                    AlertDialog.Builder(this@MainActivity).apply {
                                        setMessage("???????????????????")
                                        setPositiveButton("??????") { dialog, which ->
                                            val sharedPreferences: SharedPreferences =
                                                getSharedPreferences(
                                                    SHARED_PREFERENCES_NAME,
                                                    MODE_PRIVATE
                                                )
                                            sharedPreferences.edit().apply {
                                                putString(USER_NANE, null)
                                                putString(PASSWORD, null)
                                            }
                                            LiveDataBus.with(USER_NANE, String::class.java).value =
                                                null
                                            LiveDataBus.with(PASSWORD, String::class.java).value =
                                                null
                                            binding.drawerHeaderName.text = "?????????"
                                            binding.drawerHeaderRanking.text = "??????:-- ??????:--"
                                            headItemBeanList.removeAt(headItemBeanList.size - 1)
                                            dataList = headItemBeanList
                                            showToast("????????????")
                                        }
                                        setNegativeButton("??????", null)
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * ?????????????????????
     */
    private fun initBottomNav() {
        val fragmentList = listOf(
            HomeFragment(), SystemFragment(),
            OfficialAccountFragment(), SquareFragment(), ProjectFragment()
        )
        binding.mainVp.apply {
            isUserInputEnabled = false
            adapter = MainViewPagerAdapter(this@MainActivity, fragmentList)
        }
        //???????????????
        binding.mainBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> binding.mainVp.currentItem = 0
                R.id.squareFragment -> binding.mainVp.currentItem = 1
                R.id.officialAccountFragment -> binding.mainVp.currentItem = 2
                R.id.systemFragment -> binding.mainVp.currentItem = 3
                R.id.projectFragment -> binding.mainVp.currentItem = 4
            }
            title = it.title
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        initSide()
        initLogin()
    }
}

