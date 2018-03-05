package com.yadaniil.blogchain.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

/**
 * Created by danielyakovlev on 3/5/18.
 */

@Entity
data class PortfolioCoinEntity(
        @Id var id: Long = 0,
        var createdAt: Date = Date(),
        var buyDate: Date = Date(),
        var amountOfCoins: String = "",
        var buyPriceInFiat: String = "",
        var storageType: String = "", // Software wallet, hardware wallet or exchange
        var storageName: String = "", // Wallet name or Exchange name
        var description: String = "",
        var coin: ToOne<CoinEntity>? = null
)