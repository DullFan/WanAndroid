package com.example.wanandroid.view.activity.photo

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseLoadAdapter
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.constant.Constant
import com.example.wanandroid.constant.Constant.PHOTO_PAGE_POSITION
import com.example.wanandroid.constant.Constant.PHOTO_SELECT_DATA
import com.example.wanandroid.constant.Constant.PHOTO_SELECT_POSITION
import com.example.wanandroid.databinding.ActivityPhotoBinding
import com.example.wanandroid.databinding.ItemPhotoLayoutBinding
import com.example.wanandroid.entity.Hit
import com.example.wanandroid.view.ui.LoadingView
import com.example.wanandroid.viewmodel.LiveDataBus
import com.example.wanandroid.viewmodel.PhotoViewModel

class PhotoActivity : BaseActivity() {
    private var type: String = "cat"
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var loadingView: LoadingView
    private val dataList: ArrayList<Hit> = ArrayList()
    private lateinit var rvAdapter: BaseLoadAdapter<Hit>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = getViewModel(PhotoViewModel::class.java)
        LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value = 1
        viewModel.requestPhoto(type, LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value!!)
        loadingView = LoadingView(this, R.style.CustomDialog)
        loadingView.show()
        initSer(viewModel)
        initRv(viewModel)
        initSwitch(viewModel)
    }

    //????????????
    private fun initSer(viewModel: PhotoViewModel) {
        binding.photoSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.photoSearch.isIconified = true
                if (query?.length == 0) {
                    showToast("???????????????")
                } else {
                    query?.let {
                        dataList.clear()
                        LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value = 1
                        loadingView.show()
                        type = query
                        viewModel.requestPhoto(
                            type,
                            LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value!!
                        )
                    }
                }
                return false
            }
        })
    }

    //????????????
    private fun initSwitch(viewModel: PhotoViewModel) {
        binding.photoSwipe.setOnRefreshListener {
            binding.photoRv.visibility = View.GONE
            dataList.clear()
            LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value = 1
            viewModel.requestPhoto("cat", 1)
        }
    }

    //??????RecyclerView
    private fun initRv(viewModel: PhotoViewModel) {
        viewModel.photoLiveData.observe(this) {
            if (binding.photoRv.visibility == View.GONE) {
                binding.photoRv.visibility = View.VISIBLE
            }

            if (it.hits.isEmpty()) {
                showToast("??????????????????!!!")
            }

            if (dataList.isNotEmpty()) {
                dataList.addAll(it.hits)
                loadingView.dismiss()
                rvAdapter.dataList = dataList
                rvAdapter.flag = false
                if(it.hits.size == 0){
                    showToast("???????????????")
                }
            } else {
                loadingView.dismiss()
                dataList.addAll(it.hits)
                binding.photoRv.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    rvAdapter =
                        object : BaseLoadAdapter<Hit>(R.layout.item_photo_layout, dataList,this@PhotoActivity) {
                            @SuppressLint("CheckResult")
                            override fun onBind(
                                rvBinding: ViewDataBinding,
                                data: Hit,
                                position: Int
                            ) {
                                rvBinding as ItemPhotoLayoutBinding
                                rvBinding.itemPhotoShimmer.apply {
                                    //???????????????
                                    setShimmerColor(0x55FFFFFF)
                                    //???????????????
                                    setShimmerAngle(1)
                                    //????????????
                                    startShimmerAnimation()
                                }

                                val requestOptions: RequestOptions = RequestOptions().apply {
                                    //??????????????????
                                    placeholder(R.drawable.ic_baseline_insert_photo_24)
                                    //????????????
                                    timeout(1000 * 15)
                                    //??????????????????
                                    error(R.drawable.loading)
                                }

                                Glide.with(this@PhotoActivity)
                                    .load(data.webformatURL)
                                    .apply(requestOptions)
                                    //???????????????
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            return false.also {
                                                rvBinding.itemPhotoShimmer?.stopShimmerAnimation()
                                            }
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            dataSource: DataSource?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            return false.also {
                                                rvBinding.itemPhotoShimmer?.stopShimmerAnimation()
                                            }
                                        }
                                    })
                                    .into(rvBinding.itemPhotoImg)

                                rvBinding.itemPhotoImg.layoutParams.height = data.webformatHeight
                                rvBinding.itemPhotoName.text = data.user
                                rvBinding.itemPhotoDianNum.text = "${data.collections}"
                                rvBinding.itemPhotoLike.text = "${data.likes}"

                                rvBinding.itemPhotoShimmer.setOnClickListener {
                                    //????????????
                                    binding.photoSearch.clearFocus()
                                    LiveDataBus.with(PHOTO_SELECT_POSITION, Int::class.java).value =
                                        position
                                    LiveDataBus.with(PHOTO_SELECT_DATA, List::class.java).value =
                                        dataList
                                    myStartActivity(PhotoDetailsActivity::class.java, "????????????")
                                }
                            }
                        }
                    adapter = rvAdapter
                }
                rvAdapter.flag = false
            }
            if (binding.photoSwipe.isRefreshing) {
                showToast("????????????")
                binding.photoSwipe.isRefreshing = false
            }
            if (it.hits.isNullOrEmpty()) {
                showToast("??????????????????,????????????????????????")
            }
        }

        //??????RecyclerView????????????
        binding.photoRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) return
                if (!recyclerView.canScrollVertically(1)) {
                    rvAdapter.flag = true
                    LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value =
                        LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value?.plus(1)
                    LiveDataBus.with(PHOTO_PAGE_POSITION, Int::class.java).value?.let {
                        viewModel.requestPhoto(type, it)
                    }
                }
            }
        })
    }
}