package com.yadaniil.blogchain.utils

import android.content.Context
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.rengwuxian.materialedittext.MaterialEditText


/**
 * Created by danielyakovlev on 9/29/17.
 */

object UiHelper {

    fun changeStatusBarColor(activity: FragmentActivity, statusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
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

    fun addFiatInputFilter(editText: MaterialEditText) {
        editText.filters = arrayOf(AmountInputFilter(12, 2))
    }

    fun addCryptocurrencyInputFilter(editText: MaterialEditText) {
        editText.filters = arrayOf(AmountInputFilter(20, 8))
    }
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if(value) View.VISIBLE else View.GONE
    }