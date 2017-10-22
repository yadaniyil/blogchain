package com.yadaniil.blogchain.utils

import android.content.Context
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


/**
 * Created by danielyakovlev on 9/29/17.
 */

object UiHelper {

    fun changeStatusBarColor(activity: FragmentActivity, statusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = activity.resources.getColor(statusBarColor)
        }
    }

    fun closeKeyboard(activity: FragmentActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}