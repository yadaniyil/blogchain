package com.yadaniil.blogchain.screens.news

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.utils.XmlParser
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by danielyakovlev on 12/23/17.
 */


@InjectViewState
class NewsPresenter : MvpPresenter<NewsView>() {

    val SOURCES_TO_DOWNLOAD = 3

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun updateNews() {
        viewState.showLoading()

        var newsSourcesLinks: MutableList<String> = ArrayList()
        NewsSources.getAllSourcesByLanguage(NewsSources.SourceLanguage.ENG)
                .mapTo(newsSourcesLinks) { it.feedRssLink }
        newsSourcesLinks = newsSourcesLinks.take(SOURCES_TO_DOWNLOAD).toMutableList()

        val client = OkHttpClient()
        val rssFeeds: MutableList<String> = ArrayList()
        for(link in newsSourcesLinks) {
            val request = Request.Builder().url(link).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Timber.e(e?.message)
                    viewState.stopLoading()
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null) {
                        val xmlString = response.body()!!.string()
                        rssFeeds.add(xmlString)
                    }

                    if (rssFeeds.size == SOURCES_TO_DOWNLOAD) {
                        parseXmlFeeds(rssFeeds)
                    }
                }
            })
        }
    }

    private fun parseXmlFeeds(rssFeeds: MutableList<String>) {
        val feeds: MutableList<NewsModel> = ArrayList()
        for(feed in rssFeeds) {
            feeds.addAll(XmlParser.parseNewsFeed(feed))
        }
        feeds.sortByDescending { it.publishDate }

        viewState.stopLoading()
        viewState.showNews(feeds)
    }
}