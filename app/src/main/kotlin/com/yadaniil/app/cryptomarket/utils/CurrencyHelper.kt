package com.yadaniil.app.cryptomarket.utils

import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm

/**
 * Created by danielyakovlev on 9/19/17.
 */

object CurrencyHelper {

    fun getImageLinkForCurrency(cmcCurrencyRealm: CoinMarketCapCurrencyRealm,
                                ccCurrencies: List<CryptoCompareCurrencyRealm>): String {
        var symbolToSearch = cmcCurrencyRealm.symbol
        when {
            cmcCurrencyRealm.id == "bitcoin-cash" -> symbolToSearch = "BCH"
            cmcCurrencyRealm.id == "iota" -> symbolToSearch = "IOT"
            cmcCurrencyRealm.id == "bitconnect" -> symbolToSearch = "BCCOIN"
        }
        return ccCurrencies.find { it.name == symbolToSearch }?.imageUrl ?: ""
    }
}