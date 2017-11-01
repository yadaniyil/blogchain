package com.yadaniil.blogchain.data.db

import io.realm.DynamicRealm
import io.realm.RealmMigration



/**
 * Created by danielyakovlev on 11/1/17.
 */

class RealmDbMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema
        var currentVersion = oldVersion

        if (currentVersion == 0L) {
            schema?.get("CoinMarketCapCurrencyRealm")
                    ?.addField("isFavourite", Boolean::class.java)
            currentVersion++
        }

    }

}