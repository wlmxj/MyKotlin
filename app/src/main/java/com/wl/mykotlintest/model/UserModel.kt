package com.wl.mykotlintest.model

import androidx.databinding.ObservableField

/**
Time:2020/7/6
Author:wl
Description:
 */
data class UserModel(
    val username: ObservableField<String> = ObservableField(""),
    val password: ObservableField<String> = ObservableField("")
) {


    override fun toString(): String {
        return "UserModel(username=$username, password=$password)"
    }
}