package com.imitate.douyin.utils

import android.text.TextUtils
import android.widget.Toast
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.imitate.douyin.MyApplication

/***
 * 作者 ： 于德海
 * 时间 ： 2021/3/29 17:14
 * 描述 ：
 */
class AlertUtils {
    companion object {
        fun showToast(text: String) {
            if (TextUtils.isEmpty(text))
                return
            ThreadUtils.runOnUiThread(Runnable {
                Toast.makeText(MyApplication.nowActivity, text, Toast.LENGTH_SHORT).show()
            })
        }
        
        
        fun showDialog(title:String,content : String,call:DialogCallback) {
            var dialog = MaterialDialog(MyApplication.nowActivity!!)
                .title(text = title)
                .cancelable(false)
                .message(text = content)
                .positiveButton(text = "确定"){call}

            dialog.show()

        }
    }
}