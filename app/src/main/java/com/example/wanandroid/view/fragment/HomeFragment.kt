package com.example.wanandroid.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseLoadAdapter
import com.example.wanandroid.base.BaseFragment
import com.example.wanandroid.databinding.FragmentHomeBinding
import com.example.wanandroid.databinding.ItemListLayoutBinding
import com.example.wanandroid.entity.BannerData
import com.example.wanandroid.entity.HomeArticlesDataX
import com.example.wanandroid.view.activity.WebUrlActivity
import com.example.wanandroid.viewmodel.WanAndroidViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig.Direction.RIGHT
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener

class HomeFragment : BaseFragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: WanAndroidViewModel
    val homeArticlesList: MutableList<HomeArticlesDataX> = mutableListOf()
    var page: Int = 1
    lateinit var rvAdapter: BaseLoadAdapter<HomeArticlesDataX>


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        viewModel.requestBanner()
        initBanner()
        initBlog()
        return binding.root
    }

    /**
     * 设置内容
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initBlog() {
        viewModel.requestHomeArticles("/article/list/${page}/json")
        viewModel.homeArticlesBean.observe(viewLifecycleOwner) {
            if (it.data.datas.isEmpty()) {
                showToast("已经没有数据了")
            }
            if (homeArticlesList.size != 0) {
                homeArticlesList.addAll(it.data.datas)
                rvAdapter.dataList = homeArticlesList
                rvAdapter.flag = true
            } else {
                homeArticlesList.addAll(it.data.datas)
                binding.homeRv.apply {
                    rvAdapter = object : BaseLoadAdapter<HomeArticlesDataX>(
                        R.layout.item_list_layout,
                        homeArticlesList,
                        context
                    ) {
                        override fun onBind(
                            rvBinding: ViewDataBinding,
                            data: HomeArticlesDataX,
                            position: Int
                        ) {
                            rvBinding as ItemListLayoutBinding
                            rvBinding.data = data
                            rvBinding.root.setOnClickListener {
                                myStartActivity(
                                    WebUrlActivity::class.java,
                                    data.title,
                                    data.link
                                )
                            }
                        }
                    }
                    layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    adapter = rvAdapter
                    rvAdapter.flag = true
                }
            }
        }
        var timeOut: Long = 0


        binding.homeScrollview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // 滚动到底部监听
            if (scrollY == ((v as NestedScrollView).getChildAt(0)
                    .getMeasuredHeight() - v.getMeasuredHeight())
            ) {
                if (System.currentTimeMillis() - timeOut > 1000) {
                    rvAdapter.flag = true
                    page++
                    viewModel.requestHomeArticles("/article/list/${page}/json")
                    timeOut = System.currentTimeMillis()
                }
            }
        }
    }

    /**
     * 设置Banner
     */
    private fun initBanner() {
        viewModel.bannerBean.observe(viewLifecycleOwner) {
            binding.homeBannerTitle.text = it.data[0].title
            binding.homeBanner.apply {
                adapter = object : BannerImageAdapter<BannerData>(it.data) {
                    override fun onBindView(

                        holder: BannerImageHolder?,
                        data: BannerData?,
                        position: Int,
                        size: Int
                    ) {
                        Glide.with(context).load(data!!.imagePath).into(holder!!.imageView)
                    }
                }
                setIndicator(CircleIndicator(context))
                setIndicatorGravity(RIGHT)
                addOnPageChangeListener(object : OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                        binding.homeBannerTitle.text = it.data[position].title
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
                //选中颜色
                setIndicatorSelectedColor(getResources().getColor(R.color.white))
                //未选中颜色
                setIndicatorNormalColor(getResources().getColor(R.color.textColor))
                //设置点击事件
                setOnBannerListener { data, position ->
                    myStartActivity(
                        WebUrlActivity::class.java,
                        it.data[position].title,
                        it.data[position].url
                    )
                }
            }
        }
    }
}