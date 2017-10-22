package com.yadaniil.blogchain.main

import com.arellomobile.mvp.MvpView

interface IMainView : MvpView {
    fun showToolbarLoading()
    fun stopToolbarLoading()
    fun updateList()
    fun showChangelogDialog()
    fun showLoadingError()
    fun showLoading()
    fun hideSwipeRefreshLoading()
}