package com.example.wanandroid.view.activity.integral

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseLoadAdapter
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.databinding.ActivityIntegralBinding
import com.example.wanandroid.databinding.ItemIntegralLayoutBinding
import com.example.wanandroid.entity.IntegralDataX
import com.example.wanandroid.view.ui.LoadingView
import com.example.wanandroid.viewmodel.LiveDataBus
import com.example.wanandroid.viewmodel.WanAndroidViewModel

class Integral : BaseActivity() {
    private lateinit var binding: ActivityIntegralBinding
    private lateinit var loadingView: LoadingView
    private lateinit var viewModel: WanAndroidViewModel
    private var page: Int = 1
    private lateinit var rvAdapter: BaseLoadAdapter<IntegralDataX>
    private val integralDataXList = mutableListOf<IntegralDataX>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntegralBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        loadingView = LoadingView(this, R.style.CustomDialog)
        loadingView.show()
        binding.lifecycleOwner = this
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        binding.number = "${LiveDataBus.with(Constant.USER_INTEGRAL, String::class.java).value}"
        viewModel.requestIntegral("/lg/coin/list/${page}/json")
        viewModel.integralBean.observe(this) {
            //判断数据源中是否有数据
            if (it.data.datas.isEmpty()) {
                showToast("没有数据了哦!!!")
            }
            binding.integralNumber.visibility = View.VISIBLE
            loadingView.dismiss()
            //判断是否是第一次加载数据
            if (integralDataXList.size != 0) {
                showLog(it.toString())
                //隐藏加载更多Item
                rvAdapter.flag = false
                integralDataXList.addAll(it.data.datas)
                rvAdapter.dataList = integralDataXList
            } else {
                integralDataXList.addAll(it.data.datas)
                binding.leaderboardRv.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    rvAdapter = object : BaseLoadAdapter<IntegralDataX>(
                        R.layout.item_integral_layout,
                        integralDataXList,
                        this@Integral
                    ) {
                        override fun onBind(
                            rvBinding: ViewDataBinding,
                            data: IntegralDataX,
                            position: Int
                        ) {
                            rvBinding as ItemIntegralLayoutBinding
                            rvBinding.data = data
                            rvBinding.itemIntegralText3.text = "+${data.coinCount}"
                        }
                    }
                    adapter = rvAdapter
                }
                rvAdapter.flag = false
            }
        }
        binding.leaderboardRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    rvAdapter.flag = true
                    page++
                    viewModel.requestIntegral("/lg/coin/list/${page}/json")
                }
            }
        })
    }
}