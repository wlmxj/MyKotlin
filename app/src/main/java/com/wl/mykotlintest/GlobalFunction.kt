package com.wl.mykotlintest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
Time:2020/7/6
Author:wl
Description:
 */

inline fun <reified T: AppCompatActivity> Context.startActivity(
    vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach { intent.putExtra(it.first, it.second) }
    startActivity(intent)
}

