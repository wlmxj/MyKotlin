package com.wl.mykotlintest.ac

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.BaseActivity
import com.wl.mykotlintest.R
import com.wl.mykotlintest.BR
import com.wl.mykotlintest.adapter.StudentAdapter
import com.wl.mykotlintest.databinding.ActivityRecyclerviewBinding
import com.wl.mykotlintest.model.User
import com.wl.mykotlintest.vm.UserViewModel
import kotlinx.android.synthetic.main.activity_recyclerview.*

/**
Time:2020/7/6
Author:wl
Description:
 */
class RecyclerViewActivity :BaseActivity<ActivityRecyclerviewBinding, UserViewModel>() {
    override fun initData() {
       mDatabing.activity = this
    }

    override fun initView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        rv.adapter = StudentAdapter(initUser(),BR.user)
    }
    private lateinit var  userList : MutableList<User>
    private fun initUser(): MutableList<User> {
        userList = mutableListOf<User>()
        for (i in 1..3){
            var user = User()
            user.firstname.set("name $i")
            user.age.set(i)
            userList.add(user)
        }
        return userList
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_recyclerview
    }

    private var i = 0
    fun onClick(view : View){
//        Log.e("wl","view_id:${view.id}")
//        var user = User()
//        user.firstname.set("name ${i++}")
//        user.age.set(i++)
//        userList.add(user)
//        rv.adapter?.notifyDataSetChanged()
//
//        --------------------------------
        for ((i, user) in userList.withIndex()) {
            user.firstname.set("更改后的名字${i}")
        }


    }
}