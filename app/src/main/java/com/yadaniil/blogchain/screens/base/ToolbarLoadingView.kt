package com.yadaniil.blogchain.screens.base

import com.arellomobile.mvp.MvpView

/**
 * Created by danielyakovlev on 11/1/17.
 */

interface ToolbarLoadingView : MvpView {
    fun showToolbarLoading()
    fun stopToolbarLoading()
    fun hideSwipeRefreshLoading()
}