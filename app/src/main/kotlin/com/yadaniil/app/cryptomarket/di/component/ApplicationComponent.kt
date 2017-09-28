package com.yadaniil.app.cryptomarket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.api.*
import com.yadaniil.app.cryptomarket.data.db.AppDbHelper
import com.yadaniil.app.cryptomarket.data.prefs.SharedPrefs
import com.yadaniil.app.cryptomarket.di.module.ApplicationModule

import com.yadaniil.app.cryptomarket.di.module.DatabaseModule


import com.yadaniil.app.cryptomarket.di.module.NetModule
import com.yadaniil.app.cryptomarket.main.MainPresenter
import com.yadaniil.app.cryptomarket.mining.fragments.coins.CoinsPresenter
import com.yadaniil.app.cryptomarket.mining.fragments.miners.MinersPresenter

import dagger.Component
import io.realm.Realm
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetModule::class, DatabaseModule::class))
interface ApplicationComponent {
    fun inject(appDbHelper: AppDbHelper)
    fun inject(appApiHelper: AppApiHelper)
    fun inject(mainPresenter: MainPresenter)
    fun inject(minersPresenter: MinersPresenter)
    fun inject(coinsPresenter: CoinsPresenter)

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
