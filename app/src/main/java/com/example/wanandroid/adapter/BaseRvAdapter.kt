package com.example.wanandroid.adapter
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView



abstract class BaseRvAdapter<T>(val layoutId: Int, _dataList: List<T>, _index: Int = 0) :

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rvBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ViewHolder(rvBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.rvBinding,dataList[position],position)
    }

    abstract fun onBind(rvBinding: ViewDataBinding,data:T,position:Int)

    override fun getItemCount(): Int = dataList.size

}

