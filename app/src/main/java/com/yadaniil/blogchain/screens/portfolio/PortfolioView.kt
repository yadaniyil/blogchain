package com.yadaniil.blogchain.screens.portfolio

import com.arellomobile.mvp.MvpView

/**
 * Created by danielyakovlev on 11/3/17.
 */

interface PortfolioView : MvpView {
    fun showToolbarLoading()
    fun hideSwipeRefreshLoading()
    fun stopToolbarLoading()
    fun showLoadingError()
}