package com.yadaniil.blogchain.di.module

import android.content.Context
import com.yadaniil.blogchain.data.api.AppApiHelper
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.db.RealmDbMigration
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRealm(context: Context): Realm {
        Realm.init(context)
        val realmConfig = RealmConfiguration.Builder()
                .schemaVersion(3) // Must be bumped when the schema changes
                .migration(RealmDbMigration()) // Migration to run instead of throwing an exception
                .build()
        Realm.setDefaultConfiguration(realmConfig)
        return Realm.getInstance(realmConfig)
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