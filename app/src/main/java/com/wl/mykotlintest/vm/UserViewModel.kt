package com.wl.mykotlintest.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
Time:2020/7/6
Author:wl
Description:
 */
class UserViewModel : ViewModel() {
    val userLiveData = MutableLiveData<String>("init")
    init {
        viewModelScope.launch {
            delay(3000)
            userLiveData.value = "lanch改变了"
        }
    }
}