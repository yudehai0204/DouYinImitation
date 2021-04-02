package com.imitate.douyin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.provider.Settings
import androidx.multidex.MultiDexApplication
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import tv.danmaku.ijk.media.exo2.*


/***
 * 作者 ： 于德海
 * 时间 ： 2021/3/29 17:16
 * 描述 ：
 */
class MyApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    companion object{
        var nowActivity: Activity? = null
    }
    override fun onCreate() {
        super.onCreate()
        GSYVideoManager.instance().enableRawPlay(this);
        PlayerFactory.setPlayManager(Exo2PlayerManager().javaClass)
        CacheFactory.setCacheManager(ExoPlayerCacheManager().javaClass)
        GSYVideoType.enableMediaCodec()
        Debuger.enable()
        registerActivityLifecycleCallbacks(this)
        AppConfig.USER_ID = Settings.System.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        nowActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        nowActivity = activity
    }
}