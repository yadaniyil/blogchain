package com.yadaniil.blogchain.main

import com.yadaniil.blogchain.base.ToolbarLoadingView
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm

interface MainView : ToolbarLoadingView {
    fun updateList()
    fun showChangelogDialog()
    fun showLoadingError()
    fun showLoading()
    fun onCurrencyAddedToFavourite(currency: CoinMarketCapCurrencyRealm)
}