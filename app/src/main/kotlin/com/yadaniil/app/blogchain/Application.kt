package com.yadaniil.app.blogchain

import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.yadaniil.app.blogchain.di.component.ApplicationComponent
import com.yadaniil.app.blogchain.di.component.DaggerApplicationComponent
import com.yadaniil.app.blogchain.di.module.ApplicationModule
import com.yadaniil.app.blogchain.utils.timber.CrashReportTree
import io.fabric.sdk.android.Fabric
import io.flowup.FlowUp
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

        //Crashlytics
//        val core = CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
//                .build()
//
//        Fabric.with(this, Crashlytics.Builder().core(core).build())
//        Fabric.with(this, Crashlytics())

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

//        FlowUp.Builder.with(this)
//                .apiKey("236ce14be59f4285b56b3eb1faf55dc1")
//                .forceReports(BuildConfig.DEBUG)
//                .start()

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

