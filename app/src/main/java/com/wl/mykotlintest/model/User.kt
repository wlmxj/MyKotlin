package com.wl.mykotlintest.model

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

/**
Time:2020/7/6
Author:wl
Description:
 */
class User {
    var firstname: ObservableField<String> = ObservableField()
    var lastname: ObservableField<String> = ObservableField()
    var isAdult: ObservableBoolean = ObservableBoolean()
    var displayName: ObservableField<String> = ObservableField()
    var age: ObservableField<Int> = ObservableField()
}