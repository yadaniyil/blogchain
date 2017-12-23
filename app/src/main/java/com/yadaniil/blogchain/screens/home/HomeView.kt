package com.yadaniil.blogchain.screens.home

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.NewsModel

/**
 * Created by danielyakovlev on 11/2/17.
 */

interface HomeView : MvpView {
    fun showChangelogDialog()
    fun showLoading()
    fun stopLoading()
    fun showNews(feeds: MutableList<NewsModel>)
}