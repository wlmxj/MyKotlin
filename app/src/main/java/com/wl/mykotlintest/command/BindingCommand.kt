package com.wl.mykotlintest.command

import com.wl.mykotlintest.otherwise
import com.wl.mykotlintest.yes

/**
Time:2020/7/7
Author:wl
Description:
 */
class BindingCommand<T> {

    private var excute: BindingAction? = null
    private var cosumer: BindingConsumer<T>? = null
    private var canExecute0: BindingFunction<Boolean>? = null

    constructor(excute: BindingAction) {
        this.excute = excute
    }

    constructor(cosumer: BindingConsumer<T>) {
        this.cosumer = cosumer
    }

    fun excute() {
        if (canExcute()) {
            excute?.let {
                it.call()
            }
        }
    }

    fun excute(params: T) {
        canExcute().yes {
            cosumer?.let {
                it.call(params)
            }
        }.otherwise {

        }
    }

    fun canExcute(): Boolean {
        if (canExecute0 == null) return true
        return canExecute0?.let { it.call() }!!
    }
}