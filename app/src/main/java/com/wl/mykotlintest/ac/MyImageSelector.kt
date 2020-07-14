package com.wl.mykotlintest.ac

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.base.BaseActivity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.broadcast.BroadcastAction
import com.luck.picture.lib.broadcast.BroadcastManager
import com.luck.picture.lib.camera.CustomCameraView
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureCropParameterStyle
import com.luck.picture.lib.style.PictureParameterStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import com.luck.picture.lib.tools.ScreenUtils
import com.luck.picture.lib.tools.SdkVersionUtils
import com.luck.picture.lib.tools.ToastUtils
import com.luck.picture.lib.tools.ValueOf
import com.wl.mykotlintest.R
import com.wl.mykotlintest.adapter.GridImageAdapter
import com.wl.mykotlintest.databinding.ActivityImageSelectorBinding
import com.wl.mykotlintest.listener.DragListener
import com.wl.mykotlintest.utils.GlideCacheEngine
import com.wl.mykotlintest.utils.GlideEngine
import com.wl.mykotlintest.vm.UserViewModel
import com.wl.mykotlintest.widget.FullyGridLayoutManager
import kotlinx.android.synthetic.main.activity_image_selector.*
import kotlinx.android.synthetic.main.layout_number.*
import java.lang.ref.WeakReference
import java.util.*

/**
Time:2020/7/13
Author:wl
Description:
 */
class MyImageSelector : BaseActivity<ActivityImageSelectorBinding, UserViewModel>(),
    RadioGroup.OnCheckedChangeListener, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    private var themeId = 0
    private var maxSelectNum = 9
    private var language = -1
    private var mAdapter: GridImageAdapter? = null

    private var needScaleBig = true
    private var needScaleSmall = true

    private var isUpward = false

    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mDragListener: DragListener? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_image_selector
    }

    override fun initData() {
    }

    override fun initView() {

        themeId = R.style.picture_default_style
        getDefaultStyle()
        rgb_crop.setOnCheckedChangeListener(this)
        rgb_style.setOnCheckedChangeListener(this)
        rgb_animation.setOnCheckedChangeListener(this)
        rgb_list_anim.setOnCheckedChangeListener(this)
        rgb_photo_mode.setOnCheckedChangeListener(this)
        rgb_language.setOnCheckedChangeListener(this)
//        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler)
//        val left_back =
//            findViewById<ImageView>(R.id.left_back)
        left_back.setOnClickListener(this)
        minus.setOnClickListener(this)
        plus.setOnClickListener(this)
        cb_crop.setOnCheckedChangeListener(this)
        cb_crop_circular.setOnCheckedChangeListener(this)
        cb_compress.setOnCheckedChangeListener(this)
        tv_select_num.text = ValueOf.toString(maxSelectNum)
        val manager = FullyGridLayoutManager(
            this,
            4, GridLayoutManager.VERTICAL, false
        )
        recycler.layoutManager = manager

        recycler.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                ScreenUtils.dip2px(this, 8f), false
            )
        )
        mAdapter = GridImageAdapter(this, onAddPicClickListener)
//        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
//            mAdapter.setList(savedInstanceState.getParcelableArrayList("selectorList"))
//        }

//        List<LocalMedia> list = new ArrayList<>();
//        LocalMedia m = new LocalMedia();
//        m.setPath("https://wx1.sinaimg.cn/mw690/006e0i7xly1gaxqq5m7t8j31311g2ao6.jpg");
//        LocalMedia m1 = new LocalMedia();
//        m1.setPath("https://ww1.sinaimg.cn/bmiddle/bcd10523ly1g96mg4sfhag20c806wu0x.gif");
//        list.add(m);
//        list.add(m1);
//        mAdapter.setList(list);

//        List<LocalMedia> list = new ArrayList<>();
//        LocalMedia m = new LocalMedia();
//        m.setPath("https://wx1.sinaimg.cn/mw690/006e0i7xly1gaxqq5m7t8j31311g2ao6.jpg");
//        LocalMedia m1 = new LocalMedia();
//        m1.setPath("https://ww1.sinaimg.cn/bmiddle/bcd10523ly1g96mg4sfhag20c806wu0x.gif");
//        list.add(m);
//        list.add(m1);
//        mAdapter.setList(list);

        var list = mutableListOf<LocalMedia>()
        val m = LocalMedia()
        val m1 = LocalMedia()
        m.path = "https://wx1.sinaimg.cn/mw690/006e0i7xly1gaxqq5m7t8j31311g2ao6.jpg"
        m1.path = "https://ww1.sinaimg.cn/bmiddle/bcd10523ly1g96mg4sfhag20c806wu0x.gif"

        list.add(m)
        list.add(m1)
        mAdapter!!.setList(list)


        mAdapter!!.setSelectMax(maxSelectNum)
        recycler.adapter = mAdapter
        cb_original.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            tv_original_tips.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        cb_choose_mode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            cb_single_back.visibility = if (isChecked) View.GONE else View.VISIBLE
            cb_single_back.isChecked = !isChecked && cb_single_back.isChecked
        }
        mAdapter!!.setOnItemClickListener { _, position ->
            val selectList: List<LocalMedia> = mAdapter!!.data
            if (selectList.isNotEmpty()) {
                val media = selectList[position]
                val mimeType = media.mimeType
                when (PictureMimeType.getMimeType(mimeType)) {
                    PictureConfig.TYPE_VIDEO ->                         // 预览视频
                        PictureSelector.create(this@MyImageSelector)
                            .themeStyle(R.style.picture_default_style)
//                            .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
                            .externalPictureVideo(if (TextUtils.isEmpty(media.androidQToPath)) media.path else media.androidQToPath)
                    PictureConfig.TYPE_AUDIO ->                         // 预览音频
                        PictureSelector.create(this@MyImageSelector)
                            .externalPictureAudio(if (PictureMimeType.isContent(media.path)) media.androidQToPath else media.path)
                    else ->                         // 预览图片 可自定长按保存路径
                        //                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
                        //                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
                        //                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(this@MyImageSelector)
                            .themeStyle(R.style.picture_default_style) // xml设置主题
//                            .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
                            //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
                            .isNotPreviewDownload(true) // 预览图片长按是否可以下载
                            //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                            .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                            .openExternalPreview(position, selectList)
                }
            }
        }

        mAdapter!!.setItemLongClickListener { holder, position, v ->
            //如果item不是最后一个，则执行拖拽
            needScaleBig = true
            needScaleSmall = true
            val size: Int = mAdapter!!.data.size
            if (size != maxSelectNum) {
                mItemTouchHelper!!.startDrag(holder)
                return@setItemLongClickListener
            }
            if (holder.getLayoutPosition() !== size - 1) {
                mItemTouchHelper!!.startDrag(holder)
            }
        }

        mDragListener = object : DragListener {
            override fun deleteState(isDelete: Boolean) {
                if (isDelete) {
                    tv_delete_text.text = getString(R.string.app_let_go_drag_delete)
                    tv_delete_text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.ic_let_go_delete,
                        0,
                        0
                    )
                } else {
                    tv_delete_text.text = getString(R.string.app_drag_delete)
                    tv_delete_text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        R.drawable.picture_icon_delete,
                        0,
                        0
                    )
                }
            }

            override fun dragState(isStart: Boolean) {
                val visibility: Int = tv_delete_text.visibility
                if (isStart) {
                    if (visibility == View.GONE) {
                        tv_delete_text.animate().alpha(1f).setDuration(300).interpolator =
                            AccelerateInterpolator()
                        tv_delete_text.visibility = View.VISIBLE
                    }
                } else {
                    if (visibility == View.VISIBLE) {
                        tv_delete_text.animate().alpha(0f).setDuration(300).interpolator =
                            AccelerateInterpolator()
                        tv_delete_text.visibility = View.GONE
                    }
                }
            }
        }

        mItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.alpha = 0.7f
                }
                return makeMovementFlags(
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP
                            or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //得到item原来的position
                try {
                    val fromPosition = viewHolder.adapterPosition
                    //得到目标position
                    val toPosition = target.adapterPosition
                    val itemViewType = target.itemViewType
                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (i in fromPosition until toPosition) {
                                Collections.swap(mAdapter!!.data, i, i + 1)
                            }
                        } else {
                            for (i in fromPosition downTo toPosition + 1) {
                                Collections.swap(mAdapter!!.data, i, i - 1)
                            }
                        }
                        mAdapter!!.notifyItemMoved(fromPosition, toPosition)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (null == mDragListener) {
                        return
                    }
                    if (needScaleBig) {
                        //如果需要执行放大动画
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).duration = 100
                        //执行完成放大动画,标记改掉
                        needScaleBig = false
                        //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                        needScaleSmall = false
                    }
                    val sh: Int = recyclerView.height + tv_delete_text.height
                    val ry: Int = tv_delete_text.bottom - sh
                    if (dY >= ry) {
                        //拖到删除处
                        (mDragListener as DragListener).deleteState(true)
                        if (isUpward) {
                            //在删除处放手，则删除item
                            viewHolder.itemView.visibility = View.INVISIBLE
                            mAdapter!!.delete(viewHolder.adapterPosition)
                            resetState()
                            return
                        }
                    } else { //没有到删除处
                        if (View.INVISIBLE == viewHolder.itemView.visibility) {
                            //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                            (mDragListener as DragListener).dragState(false)
                        }
                        if (needScaleSmall) { //需要松手后才能执行
                            viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).duration = 100
                        }
                        (mDragListener as DragListener).deleteState(false)
                    }
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

            override fun onSelectedChanged(
                viewHolder: RecyclerView.ViewHolder?,
                actionState: Int
            ) {
                val itemViewType =
                    viewHolder?.itemViewType ?: GridImageAdapter.TYPE_CAMERA
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        (mDragListener as DragListener).dragState(true)
                    }
                    super.onSelectedChanged(viewHolder, actionState)
                }
            }

            override fun getAnimationDuration(
                recyclerView: RecyclerView,
                animationType: Int,
                animateDx: Float,
                animateDy: Float
            ): Long {
                needScaleSmall = true
                isUpward = true
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                val itemViewType = viewHolder.itemViewType
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.alpha = 1.0f
                    super.clearView(recyclerView, viewHolder)
                    mAdapter!!.notifyDataSetChanged()
                    resetState()
                }
            }
        })

        // 绑定拖拽事件

        // 绑定拖拽事件
        mItemTouchHelper!!.attachToRecyclerView(recycler)

        // 注册广播

        // 注册广播
        BroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            BroadcastAction.ACTION_DELETE_PREVIEW_POSITION
        )
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (TextUtils.isEmpty(action)) {
                return
            }
            if (BroadcastAction.ACTION_DELETE_PREVIEW_POSITION == action) {
                // 外部预览删除按钮回调
                val extras = intent.extras
                if (extras != null) {
                    val position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION)
                    ToastUtils.s(
                        this@MyImageSelector,
                        "delete image index:$position"
                    )
                    mAdapter!!.remove(position)
                    mAdapter!!.notifyItemRemoved(position)
                }
            }
        }
    }

    /**
     * 重置
     */
    private fun resetState() {
        if (mDragListener != null) {
            mDragListener!!.deleteState(false)
            mDragListener!!.dragState(false)
        }
        isUpward = false
    }

    private val onAddPicClickListener: GridImageAdapter.onAddPicClickListener =
        GridImageAdapter.onAddPicClickListener {
            val mode = cb_mode.isChecked
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(this@MyImageSelector)
                    .openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                    .theme(themeId) // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .isWeChatStyle(true) // 是否开启微信图片选择风格
                    .isUseCustomCamera(cb_custom_camera.isChecked) // 是否使用自定义相机
                    .setLanguage(language) // 设置语言，默认中文
                    .isPageStrategy(cbPage.isChecked) // 是否开启分页策略 & 每页多少条；默认开启
                    .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle) // 动态自定义裁剪主题
                    //                    .setPictureWindowAnimationStyle(mWindowAnimationStyle) // 自定义相册启动退出动画
                    //                    .setRecyclerAnimationMode(animationMode) // 列表动画效果
                    .isWithVideoImage(true) // 图片和视频是否可以同选,只在ofAll模式下有效
                    .isMaxSelectEnabledMask(cbEnabledMask.isChecked) // 选择数到了最大阀值列表是否启用蒙层效果
                    //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                    //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                    //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                    .setButtonFeatures(CustomCameraView.BUTTON_STATE_ONLY_CAPTURE)// 设置自定义相机按钮状态
                    .maxSelectNum(maxSelectNum) // 最大图片选择数量
                    .minSelectNum(1) // 最小选择数量
                    .maxVideoSelectNum(1) // 视频最大选择数量
                    //.minVideoSelectNum(1)// 视频最小选择数量
                    //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                    .imageSpanCount(4) // 每行显示个数
                    .isReturnEmpty(false) // 未选择数据时点击按钮是否可以返回
                    .closeAndroidQChangeWH(true) //如果图片有旋转角度则对换宽高,默认为true
                    .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q()) // 如果视频有旋转角度则对换宽高,默认为false
                    //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(cb_original.isChecked) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                    //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                    //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                    //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                    //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                    //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                    .selectionMode(if (cb_choose_mode.isChecked) PictureConfig.MULTIPLE else PictureConfig.SINGLE) // 多选 or 单选
                    .isSingleDirectReturn(cb_single_back.isChecked) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                    .isPreviewImage(cb_preview_img.isChecked) // 是否可预览图片
                    .isPreviewVideo(cb_preview_video.isChecked) // 是否可预览视频
                    //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                    .isEnablePreviewAudio(cb_preview_audio.isChecked) // 是否可播放音频
                    .isCamera(cb_isCamera.isChecked) // 是否显示拍照按钮
                    .isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                    //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                    .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                    .isEnableCrop(cb_crop.isChecked) // 是否裁剪
                    //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                    .isCompress(cb_compress.isChecked) // 是否压缩
                    //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                    .synOrAsy(true) //同步true或异步false 压缩 默认同步
                    //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                    //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                    //                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(!cb_hide.isChecked) // 是否显示uCrop工具栏，默认不显示
                    .isGif(cb_isGif.isChecked) // 是否显示gif图片
                    .freeStyleCropEnabled(cb_styleCrop.isChecked) // 裁剪框是否可拖拽
                    .circleDimmedLayer(cb_crop_circular.isChecked) // 是否圆形裁剪
                    //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                    //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                    .setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                    .showCropFrame(cb_showCropFrame.isChecked) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(cb_showCropGrid.isChecked) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(cb_voice.isChecked) // 是否开启点击声音
                    .selectionData(mAdapter!!.data) // 是否传入已选图片
                    .isDragFrame(true)// 是否可拖动裁剪框(固定)
                    //.videoMinSecond(10)// 查询多少秒以内的视频
                    //.videoMaxSecond(15)// 查询多少秒以内的视频
                    //.recordVideoSecond(10)//录制视频秒数 默认60s
                    //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                    .cutOutQuality(90) // 裁剪输出质量 默认100
                    .minimumCompressSize(100) // 小于多少kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    .rotateEnabled(true) // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    .forResult(MyResultCallback(mAdapter))
            } else {
                // 单独拍照
                PictureSelector.create(this@MyImageSelector)
                    .openCamera(PictureMimeType.ofAll()) // 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(themeId) // 主题样式设置 具体参考 values/styles
                    .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
                    //                    .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
                    //                    .setPictureCropStyle(mCropParameterStyle) // 动态自定义裁剪主题
                    //                    .setPictureWindowAnimationStyle(mWindowAnimationStyle) // 自定义相册启动退出动画
                    .maxSelectNum(maxSelectNum) // 最大图片选择数量
                    .isUseCustomCamera(cb_custom_camera.isChecked) // 是否使用自定义相机
                    //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                    .minSelectNum(1) // 最小选择数量
                    //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                    .closeAndroidQChangeWH(true) //如果图片有旋转角度则对换宽高，默认为true
                    .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q()) // 如果视频有旋转角度则对换宽高，默认false
                    .selectionMode(if (cb_choose_mode.isChecked) PictureConfig.MULTIPLE else PictureConfig.SINGLE) // 多选 or 单选
                    //.cameraFileName(System.currentTimeMillis() + ".jpg")// 使用相机时保存至本地的文件名称,注意这个只在拍照时可以使用
                    //.renameCompressFile(System.currentTimeMillis() + ".jpg")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                    //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                    .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine()) // 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                    .isPreviewImage(cb_preview_img.isChecked) // 是否可预览图片
                    .isPreviewVideo(cb_preview_video.isChecked) // 是否可预览视频
                    .isEnablePreviewAudio(cb_preview_audio.isChecked) // 是否可播放音频
                    .isCamera(cb_isCamera.isChecked) // 是否显示拍照按钮
                    .isEnableCrop(cb_crop.isChecked) // 是否裁剪
                    //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                    .isCompress(cb_compress.isChecked) // 是否压缩
                    .compressQuality(60) // 图片压缩后输出质量
                    .glideOverride(160, 160) // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    //                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(!cb_hide.isChecked) // 是否显示uCrop工具栏，默认不显示
                    .isGif(cb_isGif.isChecked) // 是否显示gif图片
                    .freeStyleCropEnabled(cb_styleCrop.isChecked) // 裁剪框是否可拖拽
                    .circleDimmedLayer(cb_crop_circular.isChecked) // 是否圆形裁剪
                    //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪背景色值
                    //.setCircleDimmedBorderColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪边框色值
                    //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                    .showCropFrame(cb_showCropFrame.isChecked) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(cb_showCropGrid.isChecked) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(cb_voice.isChecked) // 是否开启点击声音
                    .selectionData(mAdapter!!.data) // 是否传入已选图片
                    //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 废弃 改用cutOutQuality()
                    .cutOutQuality(90) // 裁剪输出质量 默认100
                    .minimumCompressSize(100) // 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    .forResult(MyResultCallback(mAdapter))
            }
        }

    companion object {
        const val TAG = "wl"
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback(adapter: GridImageAdapter?) :
        OnResultCallbackListener<LocalMedia> {
        private val mAdapterWeakReference: WeakReference<GridImageAdapter?> = WeakReference(adapter)
        override fun onResult(result: List<LocalMedia>) {
            for (media in result) {
                Log.i(TAG, "是否压缩:" + media.isCompressed)
                Log.i(TAG, "压缩:" + media.compressPath)
                Log.i(TAG, "原图:" + media.path)
                Log.i(TAG, "是否裁剪:" + media.isCut)
                Log.i(TAG, "裁剪:" + media.cutPath)
                Log.i(TAG, "是否开启原图:" + media.isOriginal)
                Log.i(TAG, "原图路径:" + media.originalPath)
                Log.i(
                    TAG,
                    "Android Q 特有Path:" + media.androidQToPath
                )
                Log.i(
                    TAG,
                    "宽高: " + media.width + "x" + media.height
                )
                Log.i(TAG, "Size: " + media.size)
                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
            }
            if (mAdapterWeakReference.get() != null) {
                mAdapterWeakReference.get()!!.setList(result)
                mAdapterWeakReference.get()!!.notifyDataSetChanged()
            }
        }

        override fun onCancel() {
            Log.i(TAG, "PictureSelector Cancel")
        }

    }

    private var mPictureParameterStyle: PictureParameterStyle? = null
    private var mCropParameterStyle: PictureCropParameterStyle? = null
    private fun getDefaultStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle!!.isChangeStatusBarFontColor = false
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle!!.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle!!.isOpenCheckNumStyle = false
        // 相册状态栏背景色
        mPictureParameterStyle!!.pictureStatusBarColor = Color.parseColor("#393a3e")
        // 相册列表标题栏背景色
        mPictureParameterStyle!!.pictureTitleBarBackgroundColor =
            Color.parseColor("#393a3e")
        // 相册父容器背景色
        mPictureParameterStyle!!.pictureContainerBackgroundColor =
            ContextCompat.getColor(this, R.color.app_color_black)
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle!!.pictureTitleUpResId = R.drawable.picture_icon_arrow_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle!!.pictureTitleDownResId = R.drawable.picture_icon_arrow_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle!!.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.picture_icon_back
        // 标题栏字体颜色
        mPictureParameterStyle!!.pictureTitleTextColor =
            ContextCompat.getColor(this, R.color.picture_color_white)
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle!!.pictureCancelTextColor =
            ContextCompat.getColor(this, R.color.picture_color_white)
        // 选择相册目录背景样式
        mPictureParameterStyle!!.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle!!.pictureCheckedStyle = R.drawable.picture_checkbox_selector
        // 相册列表底部背景色
        mPictureParameterStyle!!.pictureBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_grey)
        // 已选数量圆点背景样式
        mPictureParameterStyle!!.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle!!.picturePreviewTextColor =
            ContextCompat.getColor(this, R.color.picture_color_fa632d)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle!!.pictureUnPreviewTextColor =
            ContextCompat.getColor(this, R.color.picture_color_white)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle!!.pictureCompleteTextColor =
            ContextCompat.getColor(this, R.color.picture_color_fa632d)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle!!.pictureUnCompleteTextColor =
            ContextCompat.getColor(this, R.color.picture_color_white)
        // 预览界面底部背景色
        mPictureParameterStyle!!.picturePreviewBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_grey)
        // 外部预览界面删除按钮样式
        mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalControlStyle =
            R.drawable.picture_original_wechat_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalFontColor =
            ContextCompat.getColor(this, R.color.app_color_white)
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle!!.pictureNavBarColor = Color.parseColor("#393a3e")
        //        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//
//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(this, R.color.app_color_grey),
            ContextCompat.getColor(this, R.color.app_color_grey),
            Color.parseColor("#393a3e"),
            ContextCompat.getColor(this, R.color.app_color_white),
            mPictureParameterStyle!!.isChangeStatusBarFontColor
        )
    }

    private var aspect_ratio_x = 0
    private var aspect_ratio_y: Int = 0
    private var chooseMode = PictureMimeType.ofAll()
    private var mWindowAnimationStyle: PictureWindowAnimationStyle? = null
    private var isWeChatStyle = false
    private var animationMode = AnimationType.DEFAULT_ANIMATION
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_all -> {
                chooseMode = PictureMimeType.ofAll()
                cb_preview_img.isChecked = true
                cb_preview_video.isChecked = true
                cb_isGif.isChecked = false
                cb_preview_video.isChecked = true
                cb_preview_img.isChecked = true
                cb_preview_video.visibility = View.VISIBLE
                cb_preview_img.visibility = View.VISIBLE
                cb_compress.visibility = View.VISIBLE
                cb_crop.visibility = View.VISIBLE
                cb_isGif.visibility = View.VISIBLE
                cb_preview_audio.visibility = View.GONE
            }
            R.id.rb_image -> {
                chooseMode = PictureMimeType.ofImage()
                cb_preview_img.isChecked = true
                cb_preview_video.isChecked = false
                cb_isGif.isChecked = false
                cb_preview_video.isChecked = false
                cb_preview_video.visibility = View.GONE
                cb_preview_img.isChecked = true
                cb_preview_audio.visibility = View.GONE
                cb_preview_img.visibility = View.VISIBLE
                cb_compress.visibility = View.VISIBLE
                cb_crop.visibility = View.VISIBLE
                cb_isGif.visibility = View.VISIBLE
            }
            R.id.rb_video -> {
                chooseMode = PictureMimeType.ofVideo()
                cb_preview_img.isChecked = false
                cb_preview_video.isChecked = true
                cb_isGif.isChecked = false
                cb_isGif.visibility = View.GONE
                cb_preview_video.isChecked = true
                cb_preview_video.visibility = View.VISIBLE
                cb_preview_img.visibility = View.GONE
                cb_preview_img.isChecked = false
                cb_compress.visibility = View.GONE
                cb_preview_audio.visibility = View.GONE
                cb_crop.visibility = View.GONE
            }
            R.id.rb_audio -> {
                chooseMode = PictureMimeType.ofAudio()
                cb_preview_audio.visibility = View.VISIBLE
            }
            R.id.rb_jpan -> language = LanguageConfig.JAPAN
            R.id.rb_tw -> language = LanguageConfig.TRADITIONAL_CHINESE
            R.id.rb_us -> language = LanguageConfig.ENGLISH
            R.id.rb_ka -> language = LanguageConfig.KOREA
            R.id.rb_de -> language = LanguageConfig.GERMANY
            R.id.rb_fr -> language = LanguageConfig.FRANCE
            R.id.rb_crop_default -> {
                aspect_ratio_x = 0
                aspect_ratio_y = 0
            }
            R.id.rb_crop_1to1 -> {
                aspect_ratio_x = 1
                aspect_ratio_y = 1
            }
            R.id.rb_crop_3to4 -> {
                aspect_ratio_x = 3
                aspect_ratio_y = 4
            }
            R.id.rb_crop_3to2 -> {
                aspect_ratio_x = 3
                aspect_ratio_y = 2
            }
            R.id.rb_crop_16to9 -> {
                aspect_ratio_x = 16
                aspect_ratio_y = 9
            }
            R.id.rb_photo_default_animation -> mWindowAnimationStyle = PictureWindowAnimationStyle()
            R.id.rb_photo_up_animation -> {
                mWindowAnimationStyle = PictureWindowAnimationStyle()
                mWindowAnimationStyle!!.ofAllAnimation(
                    R.anim.picture_anim_up_in,
                    R.anim.picture_anim_down_out
                )
            }
            R.id.rb_default_style -> {
                themeId = R.style.picture_default_style
                isWeChatStyle = false
                getDefaultStyle()
            }
            R.id.rb_white_style -> {
                themeId = R.style.picture_white_style
                isWeChatStyle = false
                getWhiteStyle()
            }
            R.id.rb_num_style -> {
                themeId = R.style.picture_QQ_style
                isWeChatStyle = false
                getNumStyle()
            }
            R.id.rb_sina_style -> {
                themeId = R.style.picture_Sina_style
                isWeChatStyle = false
                getSinaStyle()
            }
            R.id.rb_we_chat_style -> {
                themeId = R.style.picture_WeChat_style
                isWeChatStyle = true
                getWeChatStyle()
            }
            R.id.rb_default -> animationMode = AnimationType.DEFAULT_ANIMATION
            R.id.rb_alpha -> animationMode = AnimationType.ALPHA_IN_ANIMATION
            R.id.rb_slide_in -> animationMode = AnimationType.SLIDE_IN_BOTTOM_ANIMATION
        }
    }

    private fun getContext() = this
    private fun getSinaStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle!!.isChangeStatusBarFontColor = true
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle!!.isOpenCompletedNumStyle = true
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle!!.isOpenCheckNumStyle = false
        // 相册状态栏背景色
        mPictureParameterStyle!!.pictureStatusBarColor = Color.parseColor("#FFFFFF")
        // 相册列表标题栏背景色
        mPictureParameterStyle!!.pictureTitleBarBackgroundColor =
            Color.parseColor("#FFFFFF")
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle!!.pictureTitleUpResId = R.drawable.ic_orange_arrow_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle!!.pictureTitleDownResId = R.drawable.ic_orange_arrow_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle!!.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.ic_back_arrow
        // 标题栏字体颜色
        mPictureParameterStyle!!.pictureTitleTextColor =
            ContextCompat.getColor(getContext(), R.color.app_color_black)
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle!!.pictureCancelTextColor =
            ContextCompat.getColor(getContext(), R.color.app_color_black)
        // 选择相册目录背景样式
        mPictureParameterStyle!!.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle!!.pictureCheckedStyle = R.drawable.picture_checkbox_selector
        // 相册列表底部背景色
        mPictureParameterStyle!!.pictureBottomBgColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_fa)
        // 已选数量圆点背景样式
        mPictureParameterStyle!!.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle!!.picturePreviewTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_fa632d)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle!!.pictureUnPreviewTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_9b)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle!!.pictureCompleteTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_fa632d)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle!!.pictureUnCompleteTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_9b)
        // 预览界面底部背景色
        mPictureParameterStyle!!.picturePreviewBottomBgColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_fa)
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalControlStyle = R.drawable.picture_original_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalFontColor =
            ContextCompat.getColor(getContext(), R.color.app_color_53575e)
        // 外部预览界面删除按钮样式
        mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle =
            R.drawable.picture_icon_black_delete
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
        //        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许俩个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "请选择";
        // 自定义相册完成文本内容，已经支持两个占位符String 但isCompleteReplaceNum必须为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;
        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(getContext(), R.color.app_color_white),
            ContextCompat.getColor(getContext(), R.color.app_color_white),
            ContextCompat.getColor(getContext(), R.color.app_color_black),
            mPictureParameterStyle!!.isChangeStatusBarFontColor
        )
    }

    private fun getWeChatStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle!!.isChangeStatusBarFontColor = false
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle!!.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle!!.isOpenCheckNumStyle = true
        // 状态栏背景色
        mPictureParameterStyle!!.pictureStatusBarColor = Color.parseColor("#393a3e")
        // 相册列表标题栏背景色
        mPictureParameterStyle!!.pictureTitleBarBackgroundColor =
            Color.parseColor("#393a3e")
        // 相册父容器背景色
        mPictureParameterStyle!!.pictureContainerBackgroundColor =
            ContextCompat.getColor(getContext(), R.color.app_color_black)
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle!!.pictureTitleUpResId = R.drawable.picture_icon_wechat_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle!!.pictureTitleDownResId = R.drawable.picture_icon_wechat_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle!!.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.picture_icon_close
        // 标题栏字体颜色
        mPictureParameterStyle!!.pictureTitleTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_white)
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle!!.pictureCancelTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_53575e)
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle!!.pictureRightDefaultTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_53575e)
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureRightSelectedTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_white)
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureUnCompleteBackgroundStyle =
            R.drawable.picture_send_button_default_bg
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg
        // 选择相册目录背景样式
        mPictureParameterStyle!!.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle!!.pictureCheckedStyle = R.drawable.picture_wechat_num_selector
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle!!.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back
        // 相册列表底部背景色
        mPictureParameterStyle!!.pictureBottomBgColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_grey)
        // 已选数量圆点背景样式
        mPictureParameterStyle!!.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle!!.picturePreviewTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_white)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle!!.pictureUnPreviewTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_9b)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle!!.pictureCompleteTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_white)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle!!.pictureUnCompleteTextColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_53575e)
        // 预览界面底部背景色
        mPictureParameterStyle!!.picturePreviewBottomBgColor =
            ContextCompat.getColor(getContext(), R.color.picture_color_half_grey)
        // 外部预览界面删除按钮样式
        mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalControlStyle =
            R.drawable.picture_original_wechat_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalFontColor =
            ContextCompat.getColor(getContext(), R.color.app_color_white)
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle!!.pictureNavBarColor = Color.parseColor("#393a3e")

        // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
//        mPictureParameterStyle.isCompleteReplaceNum = true;
        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureUnCompleteText = getString(R.string.app_wechat_send);
        //自定义相册右侧已选中时文案 支持占位符String 但只支持两个 必须isCompleteReplaceNum为true
//        mPictureParameterStyle.pictureCompleteText = getString(R.string.app_wechat_send_num);
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//        // 自定义预览页右下角选择文字文案
//        mPictureParameterStyle.pictureWeChatPreviewSelectedText = "";

//        // 自定义相册标题文字大小
//        mPictureParameterStyle.pictureTitleTextSize = 9;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 9;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 9;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 9;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 9;
//        // 自定义预览页右下角选择文字大小
//        mPictureParameterStyle.pictureWeChatPreviewSelectedTextSize = 9;

        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(getContext(), R.color.app_color_grey),
            ContextCompat.getColor(getContext(), R.color.app_color_grey),
            Color.parseColor("#393a3e"),
            ContextCompat.getColor(getContext(), R.color.app_color_white),
            mPictureParameterStyle!!.isChangeStatusBarFontColor
        )
    }

    private fun getNumStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle!!.isChangeStatusBarFontColor = false
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle!!.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle!!.isOpenCheckNumStyle = true
        // 相册状态栏背景色
        mPictureParameterStyle!!.pictureStatusBarColor = Color.parseColor("#7D7DFF")
        // 相册列表标题栏背景色
        mPictureParameterStyle!!.pictureTitleBarBackgroundColor =
            Color.parseColor("#7D7DFF")
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle!!.pictureTitleUpResId = R.drawable.picture_icon_arrow_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle!!.pictureTitleDownResId = R.drawable.picture_icon_arrow_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle!!.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.picture_icon_back
        // 标题栏字体颜色
        mPictureParameterStyle!!.pictureTitleTextColor =
            ContextCompat.getColor(this, R.color.app_color_white)
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle!!.pictureCancelTextColor =
            ContextCompat.getColor(this, R.color.app_color_white)
        // 选择相册目录背景样式
        mPictureParameterStyle!!.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle!!.pictureCheckedStyle = R.drawable.checkbox_num_selector
        // 相册列表底部背景色
        mPictureParameterStyle!!.pictureBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_fa)
        // 已选数量圆点背景样式
        mPictureParameterStyle!!.pictureCheckNumBgStyle = R.drawable.num_oval_blue
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle!!.picturePreviewTextColor =
            ContextCompat.getColor(this, R.color.picture_color_blue)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle!!.pictureUnPreviewTextColor =
            ContextCompat.getColor(this, R.color.app_color_blue)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle!!.pictureCompleteTextColor =
            ContextCompat.getColor(this, R.color.app_color_blue)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle!!.pictureUnCompleteTextColor =
            ContextCompat.getColor(this, R.color.app_color_blue)
        // 预览界面底部背景色
        mPictureParameterStyle!!.picturePreviewBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_fa)
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalControlStyle =
            R.drawable.picture_original_blue_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalFontColor =
            ContextCompat.getColor(this, R.color.app_color_blue)
        // 外部预览界面删除按钮样式
        mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
        //        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(getContext(), R.color.app_color_blue),
            ContextCompat.getColor(getContext(), R.color.app_color_blue),
            ContextCompat.getColor(getContext(), R.color.app_color_white),
            mPictureParameterStyle!!.isChangeStatusBarFontColor
        )
    }

    private fun getWhiteStyle() {
        // 相册主题
        mPictureParameterStyle = PictureParameterStyle()
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle!!.isChangeStatusBarFontColor = true
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle!!.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle!!.isOpenCheckNumStyle = false
        // 相册状态栏背景色
        mPictureParameterStyle!!.pictureStatusBarColor = Color.parseColor("#FFFFFF")
        // 相册列表标题栏背景色
        mPictureParameterStyle!!.pictureTitleBarBackgroundColor =
            Color.parseColor("#FFFFFF")
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle!!.pictureTitleUpResId = R.drawable.ic_orange_arrow_up
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle!!.pictureTitleDownResId = R.drawable.ic_orange_arrow_down
        // 相册文件夹列表选中圆点
        mPictureParameterStyle!!.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.ic_back_arrow
        // 标题栏字体颜色
        mPictureParameterStyle!!.pictureTitleTextColor =
            ContextCompat.getColor(this, R.color.app_color_black)
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle!!.pictureCancelTextColor =
            ContextCompat.getColor(this, R.color.app_color_black)
        // 选择相册目录背景样式
        mPictureParameterStyle!!.pictureAlbumStyle = R.drawable.picture_new_item_select_bg
        // 相册列表勾选图片样式
        mPictureParameterStyle!!.pictureCheckedStyle = R.drawable.picture_checkbox_selector
        // 相册列表底部背景色
        mPictureParameterStyle!!.pictureBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_fa)
        // 已选数量圆点背景样式
        mPictureParameterStyle!!.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle!!.picturePreviewTextColor =
            ContextCompat.getColor(this, R.color.picture_color_fa632d)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle!!.pictureUnPreviewTextColor =
            ContextCompat.getColor(this, R.color.picture_color_9b)
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle!!.pictureCompleteTextColor =
            ContextCompat.getColor(this, R.color.picture_color_fa632d)
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle!!.pictureUnCompleteTextColor =
            ContextCompat.getColor(this, R.color.picture_color_9b)
        // 预览界面底部背景色
        mPictureParameterStyle!!.picturePreviewBottomBgColor =
            ContextCompat.getColor(this, R.color.picture_color_white)
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalControlStyle = R.drawable.picture_original_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle!!.pictureOriginalFontColor =
            ContextCompat.getColor(this, R.color.app_color_53575e)
        // 外部预览界面删除按钮样式
        mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle =
            R.drawable.picture_icon_black_delete
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
        //        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = PictureCropParameterStyle(
            ContextCompat.getColor(this, R.color.app_color_white),
            ContextCompat.getColor(this, R.color.app_color_white),
            ContextCompat.getColor(this, R.color.app_color_black),
            mPictureParameterStyle!!.isChangeStatusBarFontColor
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.left_back -> finish()
            R.id.minus -> {
                if (maxSelectNum > 1) {
                    maxSelectNum--
                }
                tv_select_num.text = maxSelectNum.toString() + ""
                mAdapter!!.setSelectMax(maxSelectNum)
            }
            R.id.plus -> {
                maxSelectNum++
                tv_select_num.text = maxSelectNum.toString() + ""
                mAdapter!!.setSelectMax(maxSelectNum)
            }
        }
    }

    private var x = 0
    private var y: Int = 0
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.cb_crop -> {
                rgb_crop.visibility = if (isChecked) View.VISIBLE else View.GONE
                cb_hide.visibility = if (isChecked) View.VISIBLE else View.GONE
                cb_crop_circular.visibility = if (isChecked) View.VISIBLE else View.GONE
                cb_styleCrop.visibility = if (isChecked) View.VISIBLE else View.GONE
                cb_showCropFrame.visibility = if (isChecked) View.VISIBLE else View.GONE
                cb_showCropGrid.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
            R.id.cb_crop_circular -> {
                if (isChecked) {
                    x = aspect_ratio_x
                    y = aspect_ratio_y
                    aspect_ratio_x = 1
                    aspect_ratio_y = 1
                } else {
                    aspect_ratio_x = x
                    aspect_ratio_y = y
                }
                rgb_crop.visibility = if (isChecked) View.GONE else View.VISIBLE
                if (isChecked) {
                    cb_showCropFrame.isChecked = false
                    cb_showCropGrid.isChecked = false
                } else {
                    cb_showCropFrame.isChecked = true
                    cb_showCropGrid.isChecked = true
                }
            }
        }
    }
}