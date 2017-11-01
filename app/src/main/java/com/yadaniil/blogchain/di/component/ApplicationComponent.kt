package com.yadaniil.blogchain.di.component

import android.content.Context
import android.content.SharedPreferences
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.*
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import com.yadaniil.blogchain.di.module.ApplicationModule

import com.yadaniil.blogchain.di.module.DatabaseModule


import com.yadaniil.blogchain.di.module.NetModule
import com.yadaniil.blogchain.main.MainPresenter
import com.yadaniil.blogchain.mining.fragments.calculator.CalculatorPresenter
import com.yadaniil.blogchain.mining.fragments.coins.CoinsPresenter
import com.yadaniil.blogchain.mining.fragments.miners.MinersPresenter
import com.yadaniil.blogchain.watchlist.WatchlistPresenter

import dagger.Component
import io.realm.Realm
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetModule::class, DatabaseModule::class))
interface ApplicationComponent {
    fun inject(appDbHelper: AppDbHelper)
    fun inject(appApiHelper: AppApiHelper)
    fun inject(sharedPrefs: SharedPrefs)
    fun inject(mainPresenter: MainPresenter)
    fun inject(minersPresenter: MinersPresenter)
    fun inject(coinsPresenter: CoinsPresenter)
    fun inject(calculatorPresenter: CalculatorPresenter)
    fun inject(watchlistPresenter: WatchlistPresenter)

    fun app(): Application
    fun context(): Context
    fun preferences(): SharedPreferences
    fun realm(): Realm
    fun coinMarketCapService(): CoinMarketCapService
    fun cryptoCompareService(): CryptoCompareService
    fun cryptoCompareMinService(): CryptoCompareMinService
    fun whatToMineService(): WhatToMineService
    fun appApiHelper(): AppApiHelper
    fun appDbHelper(): AppDbHelper
    fun sharedPrefs(): SharedPrefs
    fun repository(): Repository
}
