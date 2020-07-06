package com.example.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inter.ICous

/**
Time:2020/7/2
Author:wl
Description:
 */
abstract class BaseActivity<DB : ViewDataBinding,VM:ViewModel> : AppCompatActivity(),
    ICous<VM> {
    lateinit var mDatabing : DB
    lateinit var mViewModel : VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabing = DataBindingUtil.setContentView<DB>(this,getLayoutId())
        mViewModel =ViewModelProvider(this).get(xxx())
        mDatabing.lifecycleOwner = this
        initData()
        initView()
    }
    abstract fun initData()
    abstract fun initView()
    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        super.onDestroy()
        mDatabing.unbind()
    }
}