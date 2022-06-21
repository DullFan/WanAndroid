package com.example.wanandroid.adapter

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wanandroid.R

const val NORMAL_VIEW_TYPE = 0
const val FOOTER_VIEW_TYPE = 1

abstract class BaseLoadAdapter<T>(
    val layoutId: Int, _dataList: List<T>,
    val context: Context, _index: Int = 0
) :
    RecyclerView.Adapter<ViewHolder>() {

    var dataList = _dataList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var index = _index
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //控制加载更多是否显示
    var flag = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var rvBinding: ViewDataBinding
        if (viewType == NORMAL_VIEW_TYPE) {
            rvBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        } else {
            rvBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_item_loading,
                parent,
                false
            )
            //设置为占据一行
            (rvBinding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                true
        }
        return ViewHolder(rvBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemCount - 1 == position) {
            if (flag) {
                holder.itemView.visibility = View.VISIBLE
            } else {
                holder.itemView.visibility = View.GONE
            }
            return
        }

        onBind(holder.rvBinding, dataList[position], position)
    }

    abstract fun onBind(rvBinding: ViewDataBinding, data: T, position: Int)

    //需要多一行给加载更多
    override fun getItemCount(): Int = dataList.size + 1

    override fun getItemViewType(position: Int): Int {
        //判断是否是最后一行
        return if (position == itemCount - 1) FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
    }
}

class ViewHolder(val rvBinding: ViewDataBinding) : RecyclerView.ViewHolder(rvBinding.root)