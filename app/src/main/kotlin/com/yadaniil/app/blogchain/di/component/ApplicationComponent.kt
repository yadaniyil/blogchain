package com.yadaniil.app.blogchain.di.component

import android.content.Context
import android.content.SharedPreferences
import com.yadaniil.app.blogchain.Application
import com.yadaniil.app.blogchain.data.Repository
import com.yadaniil.app.blogchain.data.api.*
import com.yadaniil.app.blogchain.data.db.AppDbHelper
import com.yadaniil.app.blogchain.data.prefs.SharedPrefs
import com.yadaniil.app.blogchain.di.module.ApplicationModule

import com.yadaniil.app.blogchain.di.module.DatabaseModule


import com.yadaniil.app.blogchain.di.module.NetModule
import com.yadaniil.app.blogchain.main.MainPresenter
import com.yadaniil.app.blogchain.mining.fragments.calculator.CalculatorPresenter
import com.yadaniil.app.blogchain.mining.fragments.coins.CoinsPresenter
import com.yadaniil.app.blogchain.mining.fragments.miners.MinersPresenter

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
    fun inject(calculatorPresenter: CalculatorPresenter)

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
