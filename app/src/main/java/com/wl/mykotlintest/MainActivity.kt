package com.wl.mykotlintest

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Observer
import com.example.base.BaseActivity
import com.wl.mykotlintest.ac.RecyclerViewActivity
import com.wl.mykotlintest.databinding.ActivityMainBinding
import com.wl.mykotlintest.model.UserModel
import com.wl.mykotlintest.vm.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding, UserViewModel>() {
    private val mUserModel: UserModel by lazy {
        UserModel()
    }
    override fun initData() {
        mDatabing.userModel = mUserModel
        mDatabing.userViewModel = mViewModel
        mDatabing.activity = this
        mViewModel.userLiveData.observe(this, Observer {
            Log.e("wl","livedata_result:$it")
        })
    }

    override fun initView() {
        loginBtn.setOnClickListener {
            val username = mUserModel.username.get().toString()
            val password  = mUserModel.password.get().toString()
            username.isEmpty().let {
                when(it){
                    true ->{
                        usernameLayout.error="用户名不可以为空"
                        Toast.makeText(this@MainActivity,"用户名不能为空", Toast.LENGTH_SHORT).show()
                    }

                    else->{
                        usernameLayout.isErrorEnabled = false
                    }
                }
            }
            password.isEmpty().also {
                when(it){
                    true ->{
                        passwordLayout.error="密码不可以为空"
                        Toast.makeText(this@MainActivity,"密码不能为空", Toast.LENGTH_SHORT).show()
                    }else ->{
                    passwordLayout.isErrorEnabled = false
                }
                }
            }

            Log.e("wl","name:$username---password:$password")
            tv_show.text  = "改变了。同志们哈"
        }

//        lifecycle.addObserver(GenericLifecycleObserver { source, event ->
//            Log.d(
//                FragmentActivity.TAG,
//                "onStateChanged: event =$event"
//            )
//        })

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    fun onTestClick(view:View){
        Toast.makeText(this,"d：haha",Toast.LENGTH_SHORT).show()
//        startActivitNew<RecyclerViewActivity>(this)

        startActivity<RecyclerViewActivity>("key" to "value")
    }
}
