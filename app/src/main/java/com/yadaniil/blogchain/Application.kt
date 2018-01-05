package com.yadaniil.blogchain

import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.data.db.RealmDbMigration
import com.yadaniil.blogchain.di.component.ApplicationComponent
import com.yadaniil.blogchain.di.component.DaggerApplicationComponent
import com.yadaniil.blogchain.di.module.ApplicationModule
import com.yadaniil.blogchain.utils.timber.CrashReportTree
import io.fabric.sdk.android.Fabric
import io.flowup.FlowUp
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.Protocol
import timber.log.Timber
import java.util.*


class Application : android.app.Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        //di
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportTree())
        }

        val client = OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

        val picasso = Picasso.Builder(this)
                .downloader(OkHttp3Downloader(client))
                .build()

        Picasso.setSingletonInstance(picasso)
    }

    companion object {

        var component: ApplicationComponent? = null
            private set
    }

}

