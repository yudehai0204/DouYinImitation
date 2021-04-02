package com.imitate.douyin.ui

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.imitate.douyin.R

/***
 * 作者 ： 于德海
 * 时间 ： 2021/3/30 15:00
 * 描述 ：
 */
abstract class BaseActivity : AppCompatActivity() {
    private var proDialog : MaterialDialog? = null

    fun showDialog(){
        runOnUiThread {
            dismissDialog()
            proDialog = MaterialDialog(this)
                .cancelOnTouchOutside(false)
                .cancelable(false)
                .customView(R.layout.include_progress)

            proDialog!!.show()
        }
    }

    fun dismissDialog(){
        runOnUiThread {
            if(proDialog != null){
                proDialog!!.dismiss()
                proDialog = null
            }
        }
    }

}