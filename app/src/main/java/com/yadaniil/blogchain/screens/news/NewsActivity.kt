package com.yadaniil.blogchain.screens.news

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import kotlinx.android.synthetic.main.activity_news.*
import org.jetbrains.anko.toast
import timber.log.Timber

/**
 * Created by danielyakovlev on 12/23/17.
 */

class NewsActivity : BaseActivity(), NewsView, OnNewsClick {

    @InjectPresenter
    lateinit var presenter: NewsPresenter

    private lateinit var newsAdapter: NewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNewsList()
        val currentLanguage = getString(R.string.lang_check)

        presenter.updateNews(currentLanguage)
        swipe_refresh.setOnRefreshListener {
            presenter.updateNews(currentLanguage)
        }
    }

    private fun initNewsList() {
        newsAdapter = NewsAdapter(this, this)
        news_recycler_view.layoutManager = LinearLayoutManager(this)
        news_recycler_view.adapter = newsAdapter
        news_recycler_view.setHasFixedSize(true)
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun stopLoading() {
        swipe_refresh.isRefreshing = false
    }

    override fun showNews(feeds: MutableList<NewsModel>) {
        newsAdapter.updateNews(feeds)
    }

    override fun showLoadingError() = runOnUiThread { toast(R.string.error) }

    override fun getLayout() = R.layout.activity_news

    override fun onClick(holder: ListHelper.NewsHolder, newsModel: NewsModel) {
        Navigator.toWebViewActivity(newsModel.newsLink, newsModel.sourceName, this)
    }
}