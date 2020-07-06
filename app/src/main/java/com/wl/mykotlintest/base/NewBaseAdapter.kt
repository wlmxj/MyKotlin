package com.example.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
Time:2020/7/3
Author:wl
Description:
 */
abstract class NewBaseAdapter<T>(private var itemLists:List<T>,private var brId:Int):RecyclerView.Adapter<NewBaseAdapter<T>.NewViewHolder>() {

    private var mContext: Context?= null

    inner class NewViewHolder(private var binding:ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(t:T){
            binding.setVariable(brId,t)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        if(mContext == null) mContext = parent.context
        val binding:ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),getLayoutId(),parent,false)
        return NewViewHolder(binding)
    }

    abstract fun getLayoutId():Int

    override fun getItemCount(): Int {
        return itemLists.size
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
       holder.bind(itemLists[position])
    }
}