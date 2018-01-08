package com.yadaniil.blogchain.utils

/**
 * Created by danielyakovlev on 7/15/17.
 */
class Endpoints {

    companion object {
        // region Base URLs
        val COIN_MARKET_CAP_URL = "https://api.coinmarketcap.com/v1/"
        val COIN_MARKET_CAP_GRAPHS_URL = "https://graphs.coinmarketcap.com/"

        val CRYPTO_COMPARE_URL = "https://www.cryptocompare.com/"
        val CRYPTO_COMPARE_MIN_URL = "https://min-api.cryptocompare.com/"

        val WHAT_TO_MINE_URL = "https://whattomine.com/"
        // endregion Base URLs

        // region Paths
        const val COIN_MARKET_CAP_TICKER_ENDPOINT = "ticker/"
        const val COIN_MARKET_CAP_GLOBAL_DATA_ENDPOINT = "global/"
        const val COIN_MARKET_CAP_GRAPHS_MARKETCAP_AND_VOLUME_ENDPOINT = "global/marketcap-total/"

        const val CRYPTO_COMPARE_COIN_LIST_ENDPOINT = "api/data/coinlist/"
        const val CRYPTO_COMPARE_MINERS_ENDPOINT = "api/data/miningequipment/"
        const val CRYPTO_COMPARE_PRICE_MULTI_FULL_ENDPOINT = "data/pricemultifull"

        const val WHAT_TO_MINE_GPU_COINS_ENDPOINT = "coins.json"
        const val WHAT_TO_MINE_ASIC_COINS_ENDPOINT = "asic.json"
        // endregion Paths

        // region Other
        const val NICEHASH_ICON = "https://pbs.twimg.com/profile_images/773196197595058176/ePnJvJXe.jpg"
        // endregion Other
    }
}