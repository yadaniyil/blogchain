package com.yadaniil.app.cryptomarket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.di.module.ApplicationModule

import com.yadaniil.app.cryptomarket.di.module.DatabaseModule


import com.yadaniil.app.cryptomarket.di.module.NetModule

import dagger.Component
import javax.inject.Singleton

@Singleton


@Component(modules = arrayOf(ApplicationModule::class,NetModule::class,DatabaseModule::class))




interface ApplicationComponent {
    fun app(): Application

    fun context(): Context

    fun preferences(): SharedPreferences
}
