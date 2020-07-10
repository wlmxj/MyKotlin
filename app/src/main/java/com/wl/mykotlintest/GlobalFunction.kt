package com.wl.mykotlintest

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Checkable
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
sealed class BooleanExt1<out T>////起桥梁作用的中间类，定义成协变

object Otherwise : BooleanExt1<Nothing>()//Nothing是所有类型的子类型，协变的类继承关系和泛型参数类型继承关系一致

class TransferData1<T>(val data: T) : BooleanExt1<T>()//data只涉及到了只读的操作
//声明成inline函数
inline fun <T> Boolean.yes(block: () -> T): BooleanExt1<T> = when {
    this -> {
        TransferData1(block.invoke())
    }
    else -> Otherwise
}

inline fun <T> BooleanExt1<T>.otherwise(block: () -> T): T = when (this) {
    is Otherwise ->
        block()
    is TransferData1 ->
        this.data
}

//--------------另一个写法---------------------
//sealed class BooleanExt1////起桥梁作用的中间类，定义成协变
//
//object Otherwise : BooleanExt1()//Nothing是所有类型的子类型，协变的类继承关系和泛型参数类型继承关系一致
//
//class TransferData1<T>(val data: T) : BooleanExt1()//data只涉及到了只读的操作
////声明成inline函数
//inline fun <T> Boolean.yes(block: () -> T): BooleanExt1 = when {
//    this -> {
//        TransferData1(block.invoke())
//    }
//    else -> Otherwise
//}
//
//inline fun <T> BooleanExt1.otherwise(block: () -> T): T = when (this) {
//    is Otherwise ->
//        block()
//    is TransferData1<*> ->
//        this.data as T
//}

inline fun <T : View> T.singleClick(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}
//兼容点击事件设置为this的情况
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 800) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

