package com.example.wanandroid.view.activity.leaderboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseLoadAdapter
import com.example.wanandroid.adapter.BaseRvAdapter
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.databinding.ActivityLeaderboardBinding
import com.example.wanandroid.databinding.ItemLeaderBoardLayoutBinding
import com.example.wanandroid.entity.DataX
import com.example.wanandroid.entity.RankingBean
import com.example.wanandroid.view.ui.LoadingView
import com.example.wanandroid.viewmodel.WanAndroidViewModel

class Leaderboard : BaseActivity() {
    private lateinit var binding:ActivityLeaderboardBinding
    private lateinit var viewModel:WanAndroidViewModel
    private val leaderboardList = mutableListOf<DataX>()
    private lateinit var loadingView: LoadingView
    private lateinit var rvAdapter: BaseLoadAdapter<DataX>
    private var page:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingView = LoadingView(this, R.style.CustomDialog)
        loadingView.show()
        binding.lifecycleOwner = this
        viewModel = getViewModel(WanAndroidViewModel::class.java)
        viewModel.requestLeaderboard("/coin/rank/${page}/json")
        viewModel.leaderboardBean.observe(this){
            if (it.data.datas.isEmpty()) {
                showToast("没有数据了哦!!!")
            }
            loadingView.dismiss()
            if(leaderboardList.size != 0){
                leaderboardList.addAll(it.data.datas)
                //降序排序
                leaderboardList.sortByDescending {it.coinCount}
                rvAdapter.dataList = leaderboardList
                rvAdapter.flag = false
                if(it.data.datas.isEmpty()){
                    showToast("没有更多了")
                }
            }else{
                leaderboardList.addAll(it.data.datas)
                showLog(leaderboardList.toString())
                binding.leaderboardRv.apply {
                    rvAdapter = object :BaseLoadAdapter<DataX>(R.layout.item_leader_board_layout,leaderboardList,this@Leaderboard){
                        override fun onBind(
                            rvBinding: ViewDataBinding,
                            data: DataX,
                            position: Int
                        ) {
                            rvBinding as ItemLeaderBoardLayoutBinding
                            rvBinding.data = data
                        }
                    }
                    layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
                    adapter = rvAdapter
                    rvAdapter.flag = false
                }
            }

            binding.leaderboardRv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!recyclerView.canScrollVertically(1)){
                        rvAdapter.flag = true
                        page++
                        viewModel.requestLeaderboard("/coin/rank/${page}/json")
                    }
                }
            })
        }
    }
}