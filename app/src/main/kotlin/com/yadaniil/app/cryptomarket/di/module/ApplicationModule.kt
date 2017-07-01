package com.yadaniil.app.cryptomarket.di.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.utils.RxBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var app: Application) {


    @Provides
    @Singleton
    fun provideApp(): Application = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @Singleton
    fun provideBus(): RxBus = RxBus()
}
