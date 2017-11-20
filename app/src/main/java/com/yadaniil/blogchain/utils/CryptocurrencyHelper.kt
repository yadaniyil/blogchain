package com.yadaniil.blogchain.utils

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm

/**
 * Created by danielyakovlev on 9/19/17.
 */

object CryptocurrencyHelper {

    fun getImageLinkForCurrency(cmcCurrencyRealm: CoinMarketCapCurrencyRealm?,
                                ccCurrencies: List<CryptoCompareCurrencyRealm>): String {
        return if(cmcCurrencyRealm == null) {
            ""
        } else {
            var symbolToSearch = cmcCurrencyRealm.symbol
            when {
                cmcCurrencyRealm.id == "bitcoin-cash" -> symbolToSearch = "BCH"
                cmcCurrencyRealm.id == "iota" -> symbolToSearch = "IOT"
                cmcCurrencyRealm.id == "bitconnect" -> symbolToSearch = "BCCOIN"
            }
            ccCurrencies.find { it.name == symbolToSearch }?.imageUrl ?: ""
        }
    }

    // For formatFiatPrice Bitcoin (BTC)
    fun getSymbolFromFullName(fullCoinName: String) =
            fullCoinName.substring(fullCoinName.indexOf("(") + 1, fullCoinName.indexOf(")"))

    fun getNameFromFullName(fullCoinName: String) =
            fullCoinName.substring(0, fullCoinName.indexOf("(") - 1)

    fun isCrypto(symbol: String, coins: List<CoinMarketCapCurrencyRealm>): Boolean {
        val cryptoSymbols: MutableList<String> = ArrayList()
        coins.mapTo(cryptoSymbols) { it.symbol ?: "" }
        for (coin in coins) {
            if(cryptoSymbols.contains(symbol))
                return true
        }
        return false
    }

}