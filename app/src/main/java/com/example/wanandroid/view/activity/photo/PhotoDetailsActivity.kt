package com.example.wanandroid.view.activity.photo

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.core.view.get
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BaseRvAdapter
import com.example.wanandroid.adapter.ViewHolder
import com.example.wanandroid.base.BaseActivity

import com.example.wanandroid.constant.Constant
import com.example.wanandroid.databinding.ActivityPhotoDetailsBinding
import com.example.wanandroid.databinding.ItemPhotoDetailsLayoutBinding
import com.example.wanandroid.entity.Hit
import com.example.wanandroid.viewmodel.LiveDataBus
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val REQUEST_CODE_CONTACT = 1

class PhotoDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityPhotoDetailsBinding
    var index: Int = 0
    private lateinit var bitmap: Bitmap

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSource: List<Hit> =
            LiveDataBus.with(Constant.PHOTO_SELECT_DATA, List::class.java).value as List<Hit>
        val position = LiveDataBus.with(Constant.PHOTO_SELECT_POSITION, Int::class.java).value!!
        index = position
        binding.photoDetailsVp.adapter =
            object : BaseRvAdapter<Hit>(R.layout.item_photo_details_layout, dataSource) {
                override fun onBind(rvBinding: ViewDataBinding, data: Hit, position: Int) {
                    rvBinding as ItemPhotoDetailsLayoutBinding
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
                    Glide.with(this@PhotoDetailsActivity)
                        .asBitmap()
                        .load(data.webformatURL)
                        .apply(requestOptions)
                        //???????????????
                        .listener(object : RequestListener<Bitmap> {
                            //????????????
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false.also {
                                    rvBinding.itemPhotoShimmer?.stopShimmerAnimation()
                                }
                            }

                            //????????????
                            override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false.also {
                                    rvBinding.itemPhotoShimmer?.stopShimmerAnimation()
                                }
                            }
                        })
                        .into(rvBinding.itemPhotoImg)
                }
            }
        binding.photoDetailsVp.setCurrentItem(position, false)
        binding.photoDetailsText.text = "${position + 1}/${dataSource.size}"
        binding.photoDetailsVp.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.photoDetailsText.text = "${position + 1}/${dataSource.size}"
                index = position
            }
        })

        binding.photoDetailsDownload.setOnClickListener {
            //PERMISSION_GRANTED:???????????????
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                //????????????
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_CONTACT
                )
            } else {
                //??????
                MainScope().launch {
                    savePhoto()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CONTACT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    MainScope().launch {
                        savePhoto()
                    }
                } else {
                    //?????????
                    showToast("???????????????,????????????????????????")
                }
            }
        }
    }


    //????????????
    private suspend fun savePhoto() {
        //??????????????????????????????
        withContext(Dispatchers.IO){
            //??????ViewPager?????????RecyclerView,???????????????position???Item,??????Item?????????ViewHolder
            val viewHolder = (binding.photoDetailsVp.get(0) as RecyclerView)
                .findViewHolderForAdapterPosition(binding.photoDetailsVp.currentItem) as ViewHolder
            viewHolder.rvBinding as ItemPhotoDetailsLayoutBinding
            bitmap = viewHolder.rvBinding.itemPhotoImg.drawable.toBitmap()
            //????????????,??????????????????????????????
            val saveUri = this@PhotoDetailsActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())?: kotlin.run {
                //?????????????????????,??????Toast??????
                MainScope().launch {
                    showToast("????????????")
                }
                return@withContext
            }
            //???????????????,??????use?????????????????????
            this@PhotoDetailsActivity.contentResolver.openOutputStream(saveUri).use {
                //????????????,??????????????????:???????????????,??????????????????:?????????,?????????????????????
                if(bitmap.compress(Bitmap.CompressFormat.PNG,90,it)){
                    MainScope().launch {
                        showToast("????????????")
                    }
                }else{
                    MainScope().launch {
                        showToast("????????????")
                    }
                }
            }
        }
    }
}