package com.imitate.douyin.utils

import android.app.Activity
import com.imitate.douyin.MyApplication
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/***
 * 作者 ： 于德海
 * 时间 ： 2019/12/11 0011 17:47
 * 描述 ：
 */
class ThreadUtils {
    companion object{
         private var executorService: ExecutorService? = Executors.newFixedThreadPool(4)
        fun runOnUiThread(aty: Activity, runnable: Runnable) {
            aty.runOnUiThread(runnable)
        }
        fun runOnUiThread(runnable: Runnable) {
           MyApplication.nowActivity?.runOnUiThread(runnable)
        }

        /***
         * 执行线程
         * @param runnable
         */
        fun execThread(runnable: Runnable) {
            executorService!!.execute(runnable)
        }
    }
}