package com.yadaniil.blogchain.utils

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.yadaniil.blogchain.screens.allcoins.AllCoinsActivity
import com.yadaniil.blogchain.screens.converter.ConverterActivity
import com.yadaniil.blogchain.screens.events.EventsActivity
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity
import com.yadaniil.blogchain.screens.home.HomeActivity
import com.yadaniil.blogchain.screens.mining.MiningActivity
import com.yadaniil.blogchain.screens.news.NewsActivity
import com.yadaniil.blogchain.screens.news.WebViewActivity
import com.yadaniil.blogchain.screens.portfolio.addcoin.AddToPortfolioActivity
import com.yadaniil.blogchain.screens.portfolio.PortfolioActivity
import com.yadaniil.blogchain.screens.watchlist.WatchlistActivity
import org.jetbrains.anko.*

/**
 * Created by danielyakovlev on 11/2/17.
 */

object Navigator {

    fun toMiningActivity(activity: AppCompatActivity) {
        activity.startActivity<MiningActivity>()
        activity.finish()
    }

    fun toFavoritesActivity(activity: AppCompatActivity) {
        activity.startActivity<WatchlistActivity>()
        activity.finish()
    }

    fun toHomeActivity(activity: AppCompatActivity) {
        activity.startActivity<HomeActivity>()
        activity.finish()
    }

    fun toAllCoinsActivity(activity: AppCompatActivity) {
        activity.startActivity<AllCoinsActivity>()
        activity.finish()
    }

    fun toPortfolioActivity(activity: AppCompatActivity) {
        activity.startActivity<PortfolioActivity>()
        activity.finish()
    }

    fun toAddCoinToPortfolioActivity(activity: AppCompatActivity, id: String = "") {
        activity.startActivity<AddToPortfolioActivity>("id" to id)
    }

    fun toConverterActivity(activity: AppCompatActivity) {
        activity.startActivity<ConverterActivity>()
        activity.finish()
    }

    fun toNewsActivity(activity: AppCompatActivity) {
        activity.startActivity<NewsActivity>()
        activity.finish()
    }

    fun toFindCurrencyActivity(activity: AppCompatActivity, requestCode: Int) {
        val intent = Intent(activity, FindCurrencyActivity::class.java)
        intent.putExtra("requestCode", requestCode)
        activity.startActivityForResult(intent, requestCode)
    }

    fun toWebViewActivity(url: String, toolbarTitle: String, activity: AppCompatActivity) {
        activity.startActivity<WebViewActivity>("url" to url, "title" to toolbarTitle)
    }

    fun toEventsActivity(activity: AppCompatActivity) {
        activity.startActivity<EventsActivity>()
        activity.finish()
    }

}