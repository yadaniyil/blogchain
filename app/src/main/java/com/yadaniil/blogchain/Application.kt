package com.yadaniil.blogchain

import android.content.Context
import android.support.multidex.MultiDex
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.utils.timber.CrashReportTree
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import java.util.*


class Application : android.app.Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportTree())
        }

        startKoin(this, getAllKoinModules())

        val client = OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

        val picasso = Picasso.Builder(this)
                .downloader(OkHttp3Downloader(client))
                .build()

        Picasso.setSingletonInstance(picasso)
    }
}

