package com.yadaniil.blogchain.utils

import android.os.Build
import android.support.v4.app.FragmentActivity
import android.view.WindowManager

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
}