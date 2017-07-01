package com.yadaniil.app.cryptomarket.di.module

import android.content.Context
import com.yadaniil.app.cryptomarket.data.api.AppApiHelper
import com.yadaniil.app.cryptomarket.data.db.AppDbHelper
import com.yadaniil.app.cryptomarket.data.prefs.SharedPrefs
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRealm(context: Context): Realm {
        Realm.init(context)
        return Realm.getDefaultInstance()
    }

    @Singleton
    @Provides
    fun provideAppApiHelper() = AppApiHelper()

    @Singleton
    @Provides
    fun provideAppDbHelper() = AppDbHelper()

    @Singleton
    @Provides
    fun provideSharedPrefs() = SharedPrefs()
}