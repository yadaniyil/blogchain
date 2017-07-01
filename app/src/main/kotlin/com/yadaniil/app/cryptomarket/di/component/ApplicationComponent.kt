package com.yadaniil.app.cryptomarket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.api.AppApiHelper
import com.yadaniil.app.cryptomarket.data.api.RetrofitService
import com.yadaniil.app.cryptomarket.data.db.AppDbHelper
import com.yadaniil.app.cryptomarket.data.prefs.SharedPrefs
import com.yadaniil.app.cryptomarket.di.module.ApplicationModule

import com.yadaniil.app.cryptomarket.di.module.DatabaseModule


import com.yadaniil.app.cryptomarket.di.module.NetModule

import dagger.Component
import io.realm.Realm
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetModule::class, DatabaseModule::class))
interface ApplicationComponent {
    fun inject(appDbHelper: AppDbHelper)
    fun inject(appApiHelper: AppApiHelper)

    fun app(): Application
    fun context(): Context
    fun preferences(): SharedPreferences
    fun realm(): Realm
    fun retrofitService(): RetrofitService
    fun appApiHelper(): AppApiHelper
    fun appDbHelper(): AppDbHelper
    fun sharedPrefs(): SharedPrefs
    fun repository(): Repository
}
