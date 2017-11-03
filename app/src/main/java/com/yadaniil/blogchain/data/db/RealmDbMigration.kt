package com.yadaniil.blogchain.data.db

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import java.util.*


/**
 * Created by danielyakovlev on 11/1/17.
 */

class RealmDbMigration : RealmMigration {



    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema
        var currentVersion = oldVersion.toInt()

        // Add isFavourite field to coin. Db version must be 2
        if (currentVersion == 1) {
            schema?.get("CoinMarketCapCurrencyRealm")
                    ?.addField("isFavourite", Boolean::class.java)
            currentVersion++
        }

        // Add Portfolio model. Db version must be 3
        if (currentVersion == 2) {
            schema?.create("PortfolioRealm")
                    ?.addField("id", String::class.java, FieldAttribute.PRIMARY_KEY)
                    ?.addField("createdAt", Date::class.java)
                    ?.addField("amountOfCoins", String::class.java)
                    ?.addField("buyPriceInFiat", String::class.java)
                    ?.addField("storageName", String::class.java)
                    ?.addRealmObjectField("coin", schema.get("CoinMarketCapCurrencyRealm"))

            currentVersion++
        }


    }



}