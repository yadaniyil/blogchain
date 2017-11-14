package com.yadaniil.blogchain.screens.base

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 11/1/17.
 */

interface CoinClickListener {
    fun onClick(holder: ListHelper.CoinViewHolder, currencyRealm: CoinMarketCapCurrencyRealm)
}