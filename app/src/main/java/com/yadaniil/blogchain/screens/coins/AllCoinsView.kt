package com.yadaniil.blogchain.screens.coins

import com.yadaniil.blogchain.screens.base.ToolbarLoadingView
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm

interface AllCoinsView : ToolbarLoadingView {
    fun updateList()
    fun showLoadingError()
    fun showLoading()
    fun onCurrencyAddedToFavourite(currency: CoinMarketCapCurrencyRealm)
}