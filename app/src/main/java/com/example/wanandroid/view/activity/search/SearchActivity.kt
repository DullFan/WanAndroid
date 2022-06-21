package com.example.wanandroid.view.activity.search

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import com.example.wanandroid.R
import com.example.wanandroid.base.BaseActivity
import com.example.wanandroid.databinding.ActivitySearchBinding
import com.google.android.material.chip.Chip

class SearchActivity : BaseActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchSearch.isIconified = true
                if (query!!.isNotEmpty()) {
                    list.add(query)
                    initData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun initData() {
        binding.searchChipGroup.removeAllViews()
        showLog(list.toString())
        for (i in 0 until list.size) {
            val inflate = LayoutInflater.from(this).inflate(R.layout.item_search_layout, null)
            val chip: Chip = inflate.findViewById(R.id.item_search_chip)
            chip.text = list.get(i)
            binding.searchChipGroup.addView(chip)
            chip.setOnCloseIconClickListener {
                list.removeAt(i)
                binding.searchChipGroup.removeViewAt(i)
                initData()
            }
        }
    }
}