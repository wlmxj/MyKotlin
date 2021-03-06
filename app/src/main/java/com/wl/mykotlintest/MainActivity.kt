package com.wl.mykotlintest

//import com.blankj.common.helper.PermissionHelper


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.example.base.BaseActivity
import com.king.zxing.CaptureActivity
import com.wl.mykotlintest.ac.*
import com.wl.mykotlintest.databinding.ActivityMainBinding
import com.wl.mykotlintest.model.UserModel
import com.wl.mykotlintest.utils.PathUtils
import com.wl.mykotlintest.vm.UserViewModel
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding, UserViewModel>() {
    companion object{
        const val REQUEST_CODE_CHOOSE = 100
    }
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
        bt_click.singleClick (10000){
            LogUtils.e("wl","click")
        }
        bt_zixing.singleClick {
            //跳转的默认扫码界面

            //跳转的默认扫码界面https://www.jianshu.com/p/168bef5526ae
//            startActivityForResult(Intent(this, MyCaptureActivity::class.java), 200)

            startActivity<QRCodeActivity>()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    fun onTestClick(view:View){
        Toast.makeText(this,"的看来发静安寺会计法圣诞快乐放假啊上课兼电费卡设计费杰卡斯都结束了咖啡机爱斯达克连接发" +
                "拉开金飞达手机开发" +
                "大杀四方静安寺令肌肤的撒娇疯狂夺金阿斯蒂芬就打上来就大家可理发师积分多少a" +
                "几率大激发上课兼大家可劳动法静安寺打开来得及啊",Toast.LENGTH_SHORT).show()
//        startActivity<RecyclerViewActivity>("key" to "value")
    }

    fun requestPermision(view:View){
        PermissionUtils.permission(PermissionConstants.STORAGE,PermissionConstants.CAMERA)
//            .rationale { activity, shouldRequest -> PermissionHelper.showRationaleDialog(activity, shouldRequest) }
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(permissionsGranted: List<String>) {
                    LogUtils.d(permissionsGranted)
                    if (permissionsGranted.size == 2) {
                    }
//                    itemsView.updateItems(bindItems())
//                    startImagePicker()//第一种方式
                    //第二种方式
                    startActivity(Intent(this@MainActivity,MyImageSelector::class.java))
                }

                override fun onDenied(permissionsDeniedForever: List<String>,
                                      permissionsDenied: List<String>) {
                    LogUtils.d(permissionsDeniedForever, permissionsDenied)
                    if (permissionsDeniedForever.isNotEmpty()) {
                        LogUtils.d("Calendar or Microphone is denied forever")
                    } else {
                        LogUtils.d("Calendar or Microphone is denied")
                    }
//                    itemsView.updateItems(bindItems())
                }
            })
            .request()
    }

    private fun startImagePicker() {
        Matisse.from(this@MainActivity)
            .choose(MimeType.ofImage(), false) // 选择 mime 的类型
            .countable(true)
            .capture(true)
            .maxSelectable(9) // 图片选择的最多数量
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .captureStrategy(CaptureStrategy(true,"com.wl.mykotlintest.MyFileProvider"))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f) // 缩略图的比例
            .imageEngine(GlideEngine()) // 使用的图片加载引擎
            .forResult(REQUEST_CODE_CHOOSE) // 设置作为标记的请求码

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            val obtainResult = Matisse.obtainResult(data)
           val result2 =  Matisse.obtainPathResult(data)
//            Log.d("Matisse", "mSelected: " + mSelected);
            image_view.setImageBitmap(PathUtils.getLoacalBitmap(result2[0]))
            LogUtils.e("result:${result2[0]}")
//            val bitmap = BitmapFactory.decodeStream(
//                contentResolver
//                .openInputStream(obtainResult[0]))
//            //将裁剪后的图片显示出来
//            image_view.setImageBitmap(bitmap)

            LogUtils.d("嗯嗯${obtainResult.toString()}")
        }
    }

    fun selectPic(view:View){
        startActivity<SimplePictuerActivity>()
    }
}
