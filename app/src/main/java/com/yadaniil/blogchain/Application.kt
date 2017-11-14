package com.yadaniil.blogchain

import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.yadaniil.blogchain.data.db.RealmDbMigration
import com.yadaniil.blogchain.di.component.ApplicationComponent
import com.yadaniil.blogchain.di.component.DaggerApplicationComponent
import com.yadaniil.blogchain.di.module.ApplicationModule
import com.yadaniil.blogchain.utils.timber.CrashReportTree
import io.fabric.sdk.android.Fabric
import io.flowup.FlowUp
import io.realm.RealmConfiguration
import timber.log.Timber


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
    }

    companion object {

        var component: ApplicationComponent? = null
            private set
    }

}

