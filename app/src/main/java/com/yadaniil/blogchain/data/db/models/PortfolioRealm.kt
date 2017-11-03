package com.yadaniil.blogchain.data.db.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by danielyakovlev on 11/3/17.
 */

open class PortfolioRealm(
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var createdAt: Date = Date(),
        var amountOfCoins: String? = "",
        var buyPriceInFiat: String? = "",
        var storageType: String? = "", // Wallet, exchange and etc.
        var storageName: String? = "", // Exchange or wallet name
        var coin: CoinMarketCapCurrencyRealm? = null
) : RealmObject()