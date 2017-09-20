package com.yadaniil.app.cryptomarket.utils

/**
 * Created by danielyakovlev on 7/15/17.
 */
class Endpoints {

    companion object {
        val COIN_MARKET_CAP_URL = "https://api.coinmarketcap.com/v1/"
        val CRYPTO_COMPARE_URL = "https://www.cryptocompare.com/"
        val CRYPTO_COMPARE_MIN_URL = "https://min-api.cryptocompare.com/"

        const val CRYPTO_COMPARE_COIN_LIST_ENDPOINT = "api/data/coinlist/"
        const val CRYPTO_COMPARE_MINERS_ENDPOINT = "api/data/miningequipment/"
        const val COIN_MARKET_CAP_TICKER_ENDPOINT = "ticker/"
        const val CRYPTO_COMPARE_PRICE_MULTI_FULL_ENDPOINT = "data/pricemultifull"
    }
}