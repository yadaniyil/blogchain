package com.yadaniil.blogchain

import android.os.Environment
import android.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.AppApiHelper
import com.yadaniil.blogchain.data.api.services.*
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.objectbox.MyObjectBox
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import com.yadaniil.blogchain.screens.allcoins.AllCoinsViewModel
import com.yadaniil.blogchain.utils.Endpoints
import io.objectbox.BoxStore
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by danielyakovlev on 3/5/18.
 */

fun getAllKoinModules() = listOf(appModule, netModule, dbModule, viewModelModule)

val appModule = applicationContext {
    bean { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }
}

val dbModule = applicationContext {
    bean { MyObjectBox.builder().androidContext(androidApplication()).build() }

    bean { AppApiHelper(get(), get(), get(), get(), get(), get()) }
    bean { SharedPrefs(get()) }
    bean {
        AppDbHelper(
                get<BoxStore>().boxFor(CoinEntity::class.java),
                get<BoxStore>().boxFor(PortfolioCoinEntity::class.java)
        )
    }
    bean { Repository(get(), get(), get()) }
}

val viewModelModule = applicationContext {
        viewModel { AllCoinsViewModel(get()) }
}

val netModule = applicationContext {
    bean { createWebService<CoinMarketCapService>(createOkHttpClient(), Endpoints.COIN_MARKET_CAP_URL) }
    bean { createWebService<CoinMarketCapGraphsService>(createOkHttpClient(), Endpoints.COIN_MARKET_CAP_GRAPHS_URL) }
    bean { createWebService<CryptoCompareService>(createOkHttpClient(), Endpoints.CRYPTO_COMPARE_URL) }
    bean { createWebService<CryptoCompareMinService>(createOkHttpClient(), Endpoints.CRYPTO_COMPARE_MIN_URL) }
    bean { createWebService<WhatToMineService>(createOkHttpClient(), Endpoints.WHAT_TO_MINE_URL) }
    bean { createWebService<CoindarService>(createOkHttpClient(), Endpoints.COINDAR_URL) }
}

private fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    val cache = Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)
    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor).build()
}

private inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val gsonBuilder = GsonBuilder()
    val gson = gsonBuilder.setFieldNamingPolicy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient().create()

    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}