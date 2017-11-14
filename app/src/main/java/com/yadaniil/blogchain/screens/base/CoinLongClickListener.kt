package com.yadaniil.blogchain.screens.base

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 11/3/17.
 */

interface CoinLongClickListener {
    fun onLongClick(holder: ListHelper.CoinViewHolder, currencyRealm: CoinMarketCapCurrencyRealm)
}