package com.imitate.douyin.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.imitate.douyin.AppConfig
import com.imitate.douyin.AppConstants
import com.imitate.douyin.R
import com.imitate.douyin.data.VideoBean
import com.imitate.douyin.utils.AlertUtils
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_video.view.*

class MainActivity : BaseActivity() {
    var TAG = AppConstants.TAG
    lateinit var adapter: Adapter
    lateinit var layoutManager: LinearLayoutManager
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list = arrayListOf<VideoBean>()
        list.add(
            VideoBean(
                R.mipmap.img1, "大钟寺", R.mipmap.img_dzs, "皇家佛教寺庙，藏有“钟王”永乐大钟和曾侯乙编钟"
                , RawResourceDataSource.buildRawResourceUri(R.raw.video1).toString(), arrayListOf("70005042")
            )
        )
        list.add(
            VideoBean(
                R.mipmap.img2, "围棋", R.mipmap.icon_wq, "小定式双飞燕。 ", RawResourceDataSource.buildRawResourceUri(R.raw.video2).toString(),
                arrayListOf("70003277")
            )
        )
        list.add(
            VideoBean(
                R.mipmap.img3, "象棋", R.mipmap.icon_xq, "古谱名局。", RawResourceDataSource.buildRawResourceUri(R.raw.video3).toString(),
                arrayListOf("70001628")
            )
        )
        list.add(
            VideoBean(
                R.mipmap.img4, "做面食", R.mipmap.icon_zms, "教你做美美哒面食。 ", RawResourceDataSource.buildRawResourceUri(R.raw.video4).toString(),
                arrayListOf("70004831")
            )
        )
        list.add(
            VideoBean(
                R.mipmap.img5, "===", R.mipmap.icon_zms, "11==233。 ", RawResourceDataSource.buildRawResourceUri(R.raw.video5).toString(),
                arrayListOf("70004831")
            )
        )

        adapter = Adapter(list)
        viewPager2.adapter = adapter
        var recyclerView: RecyclerView = viewPager2.getChildAt(0) as RecyclerView
        layoutManager =
            recyclerView.layoutManager as LinearLayoutManager
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                this@MainActivity.position = position
                super.onPageSelected(position)
                Log.i(AppConstants.TAG, "position --" + position + ":" + layoutManager.childCount)
                startVideo()
            }
        })

    }

    fun startVideo() {
        GSYVideoManager.releaseAllVideos()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            layoutManager.findViewByPosition(position)?.gsyVideo?.isLooping = true
            layoutManager.findViewByPosition(position)?.gsyVideo?.startPlayLogic()
            Log.i(
                TAG,
                "position == " + position + " la=" + layoutManager + ";" + layoutManager.findViewByPosition(
                    position
                )?.gsyVideo?.getUrl()
            )

            layoutManager.findViewByPosition(position)?.gsyVideo?.requestFocus()
        }, 200)

//        showDialog()
//        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//            dismissDialog()
//        }, 2000)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_UP) {
            when (event!!.keyCode) {
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (viewPager2.currentItem < adapter.itemCount) {
                        viewPager2.currentItem = viewPager2.currentItem + 1
                    }
                }
                KeyEvent.KEYCODE_DPAD_UP -> {
                    if (viewPager2.currentItem > 0) {
                        viewPager2.currentItem = viewPager2.currentItem - 1
                    }
                }
                KeyEvent.KEYCODE_BACK -> {
                    finish()
                }

            }
        } else {

        }
        if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER
            || event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
            || event.keyCode == KeyEvent.KEYCODE_VOLUME_UP
            || event.keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
            || event.keyCode == KeyEvent.KEYCODE_MUTE
        ) {
            return super.dispatchKeyEvent(event)
        }

        return true
    }


    class Adapter(list: ArrayList<VideoBean> = arrayListOf()) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {
        var mList = list;
        lateinit var context: Context

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context = parent.context
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tv_app_name.text = mList[position].name
            holder.itemView.tv_content.text = mList[position].content
            holder.itemView.img_icon.setImageResource(mList[position].icon)
            var cache = true
            if (mList[position].path.contains("rawresource") || mList[position].path.startsWith("android")) {
                cache = false
            }
            holder.itemView.gsyVideo.setUp(mList[position].path, cache, "")
            var img = ImageView(context)
            img.setImageResource(mList[position].thumb)
            img.scaleType = ImageView.ScaleType.FIT_CENTER
            holder.itemView.gsyVideo.thumbImageView = img
            holder.itemView.gsyVideo.playPosition = position
            holder.itemView.gsyVideo.isLooping = true
            holder.itemView.btn_go.setOnClickListener { v: View? ->

                if (mList[position].appID.size <= 0) {
                    AlertUtils.showToast("当前分类无游戏")
                    return@setOnClickListener
                }
                if (mList[position].appID.size == 1) {
                    goPlayer(mList[position].appID[0])
                    return@setOnClickListener
                }
                MaterialDialog(context)
                    .title(text = "选择游戏")
                    .listItems(items = mList[position].appID) { dialog, index, text ->
                        goPlayer(mList[position].appID[index])
                    }.show()

            }
        }


        private fun goPlayer(id: String) {
            AppConfig.APP_ID = id
            AppConfig.SHOW_STICK = ("70003277" == AppConfig.APP_ID)
//            context.startActivity(
//                Intent(
//                    context,
//                    HPlayerActivity::class.java
//                )
//            )
        }

    }


    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause();
    }

    override fun onResume() {
        super.onResume()
        if (this::layoutManager.isInitialized)
            startVideo()

    }

    override fun onStop() {
        super.onStop()
        GSYVideoManager.releaseAllVideos()
    }


}