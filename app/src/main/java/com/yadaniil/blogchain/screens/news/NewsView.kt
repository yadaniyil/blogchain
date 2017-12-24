package com.yadaniil.blogchain.screens.news

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.NewsModel

/**
 * Created by danielyakovlev on 12/23/17.
 */


interface NewsView : MvpView {
    fun showLoading()
    fun stopLoading()
    fun showNews(feeds: MutableList<NewsModel>)
    fun showLoadingError()
}