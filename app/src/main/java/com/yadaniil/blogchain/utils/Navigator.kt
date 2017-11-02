package com.yadaniil.blogchain.utils

import android.support.v7.app.AppCompatActivity
import com.yadaniil.blogchain.home.HomeActivity
import com.yadaniil.blogchain.mining.MiningActivity
import com.yadaniil.blogchain.watchlist.WatchlistActivity
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

}