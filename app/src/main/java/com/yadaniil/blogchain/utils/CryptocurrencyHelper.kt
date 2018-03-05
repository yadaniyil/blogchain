package com.yadaniil.blogchain.utils

import com.yadaniil.blogchain.data.db.models.CoinEntity

/**
 * Created by danielyakovlev on 9/19/17.
 */

object CryptocurrencyHelper {

    // For formatFiatPrice Bitcoin (BTC)
    fun getSymbolFromFullName(fullCoinName: String) =
            fullCoinName.substring(fullCoinName.indexOf("(") + 1, fullCoinName.indexOf(")"))

    fun getNameFromFullName(fullCoinName: String) =
            fullCoinName.substring(0, fullCoinName.indexOf("(") - 1)

    fun isCrypto(symbol: String, coins: List<CoinEntity>): Boolean {
        val cryptoSymbols: MutableList<String> = ArrayList()
        coins.mapTo(cryptoSymbols) { it.symbol ?: "" }
        for (coin in coins) {
            if(cryptoSymbols.contains(symbol))
                return true
        }
        return false
    }

}