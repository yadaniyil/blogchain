package com.yadaniil.app.cryptomarket

import com.yadaniil.app.cryptomarket.utils.timber.CrashReportTree
import timber.log.Timber
//import com.crashlytics.android.Crashlytics
//import io.fabric.sdk.android.Fabric
//import com.crashlytics.android.core.CrashlyticsCore
import com.yadaniil.app.cryptomarket.di.module.ApplicationModule
import com.yadaniil.app.cryptomarket.di.component.ApplicationComponent
import com.yadaniil.app.cryptomarket.di.component.DaggerApplicationComponent


class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        //di
        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()

        //Crashlytics
//        val core = CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
//                .build()
//
//        Fabric.with(this, Crashlytics.Builder().core(core).build())
//
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        } else {
//            Timber.plant(CrashReportTree())
//        }
    }

    companion object {

        var component: ApplicationComponent? = null
            private set
    }

}

