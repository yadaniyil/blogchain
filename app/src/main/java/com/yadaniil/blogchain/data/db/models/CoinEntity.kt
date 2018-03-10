package com.yadaniil.blogchain.data.db.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by danielyakovlev on 3/5/18.
 */

@Entity
data class CoinEntity(
        @Id var id: Long = 0,

        // CoinMarkeyCap fields
        var cmcId: String = "",
        var name: String = "",
        var symbol: String = "",
        var rank: Int = 0,
        var priceUsd: Double = 0.0,
        var priceBtc: Double = 0.0,
        var volume24hUsd: Double = 0.0,
        var marketCapUsd: Double = 0.0,
        var availableSupply: Double = 0.0,
        var totalSupply: Double = 0.0,
        var maxSupply: Double = 0.0,
        var percentChange1h: Double = 0.0,
        var percentChange24h: Double = 0.0,
        var percentChange7d: Double = 0.0,
        var lastUpdated: Long = 0L,
        var iconBytes: ByteArray? = null,
        var isFavourite: Boolean = false,
        var priceFiatAnalogue: Double = 0.0,
        var volume24hFiatAnalogue: Double = 0.0,
        var marketCapFiatAnalogue: Double = 0.0,

        // CryptoCompare fields
        var ccUrl: String = "",
        var ccImageUrl: String = "",
        var ccName: String = "",
        var ccCoinName: String = "",
        var ccFullName: String = "",
        var algorithm: String = "",
        var proofType: String = "",
        var fullyPremined: String = "",
        var totalCoinSupply: String = "",
        var preminedValue: String = "",
        var totalCoinsFreeFloat: String = "",
        var sortOrder: Long = 0L
)