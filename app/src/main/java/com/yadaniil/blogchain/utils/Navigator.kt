package com.yadaniil.blogchain.utils

import android.support.v7.app.AppCompatActivity
import com.yadaniil.blogchain.screens.allcoins.AllCoinsActivity
import com.yadaniil.blogchain.screens.converter.ConverterActivity
import com.yadaniil.blogchain.screens.home.HomeActivity
import com.yadaniil.blogchain.screens.mining.MiningActivity
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

    fun toWatchlistActivity(activity: AppCompatActivity) {
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

}