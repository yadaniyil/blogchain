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
        var buyDate: Date = Date(),
        var amountOfCoins: String? = "",
        var buyPriceInFiat: String? = "",
        var storageType: String? = "", // Software wallet, hardware wallet or exchange
        var storageName: String? = "", // Wallet name or Exchange name
        var coin: CoinMarketCapCurrencyRealm? = null
) : RealmObject()