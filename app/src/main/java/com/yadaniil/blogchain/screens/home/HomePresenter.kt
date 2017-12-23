package com.yadaniil.blogchain.screens.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.BuildConfig
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.home.news.NewsSources
import com.yadaniil.blogchain.utils.XmlParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject
import java.io.IOException


/**
 * Created by danielyakovlev on 11/2/17.
 */

@InjectViewState
class HomePresenter : MvpPresenter<HomeView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getPortfolios() = repo.getAllPortfolio()
    fun getAllCoins() = repo.getAllCoinsFromDb()

    fun showChangelogDialog() {
        if (repo.getLastShowChangelogVersion() != BuildConfig.VERSION_CODE) {
            viewState.showChangelogDialog()
            repo.setLastShowChangelogVersion(BuildConfig.VERSION_CODE)
        }
    }

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { viewState.showLoading() }
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { downloadCMCList() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCoinsToDb(currenciesList)
                }, { error ->
                    Timber.e(error.message)
                })
    }

    private fun downloadCMCList() {
        repo.getAllCoins(limit = "0")
                .subscribeOn(Schedulers.io())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { viewState.stopLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinsToDb(currenciesList)
                }, { error ->
                    Timber.e(error.message)
                })
    }

    fun downloadNews() {
        var newsSourcesLinks: MutableList<String> = ArrayList()
        NewsSources.getAllSourcesByLanguage(NewsSources.SourceLanguage.ENG)
                .mapTo(newsSourcesLinks) { it.feedRssLink }
        newsSourcesLinks = newsSourcesLinks.take(1).toMutableList()

        Timber.e("Feeds links: " + newsSourcesLinks)

        val client = OkHttpClient()
        val rssFeeds: MutableList<String> = ArrayList()
        for(link in newsSourcesLinks) {
            val request = Request.Builder().url(link).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Timber.e(e?.message)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null) {
                        val xmlString = response.body()!!.string()
                        rssFeeds.add(xmlString)
                    }

//                    if (rssFeeds.size == 3) {
                        parseXmlFeeds(rssFeeds)
//                    }
                }
            })
        }
    }

    private fun parseXmlFeeds(rssFeeds: MutableList<String>) {
        val feeds: MutableList<NewsModel> = ArrayList()
        for(feed in rssFeeds) {
            feeds.addAll(XmlParser.parseNewsFeed(feed))
        }
        viewState.showNews(feeds)
        Timber.e("feeds size: " + feeds.size)
    }

}