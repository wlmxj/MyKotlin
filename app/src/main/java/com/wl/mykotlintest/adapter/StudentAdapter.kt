package com.wl.mykotlintest.adapter

import com.example.base.NewBaseAdapter
import com.wl.mykotlintest.R
import com.wl.mykotlintest.model.User

/**
Time:2020/7/6
Author:wl
Description:
 */
class StudentAdapter(itemList: List<User>, brId: Int): NewBaseAdapter<User>(itemList,brId){

    override fun getLayoutId(): Int {
        return R.layout.recycler_view_item
    }


}