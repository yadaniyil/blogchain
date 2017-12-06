package com.yadaniil.blogchain.screens.home

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm

/**
 * Created by danielyakovlev on 12/5/17.
 */

sealed class HomeListItem
data class CoinsItem(val coin: List<CoinMarketCapCurrencyRealm>) : HomeListItem()
data class NewsItem(val header: String) : HomeListItem()
data class PortfolioItem(val balanceUsd: String) : HomeListItem()