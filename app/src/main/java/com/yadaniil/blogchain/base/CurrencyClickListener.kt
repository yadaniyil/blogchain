package com.yadaniil.blogchain.base

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.CurrencyListHelper

/**
 * Created by danielyakovlev on 11/1/17.
 */

interface CurrencyClickListener {

    fun onClick(holder: CurrencyListHelper.CurrencyViewHolder, currencyRealm: CoinMarketCapCurrencyRealm)
}