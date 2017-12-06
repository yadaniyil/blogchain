package com.yadaniil.blogchain.screens.home

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 12/5/17.
 */

sealed class HomeListItem
data class CoinsItem(val coin: RealmResults<CoinMarketCapCurrencyRealm>) : HomeListItem()
data class NewsItem(val header: String) : HomeListItem()
data class PortfolioItem(val portfolios: RealmResults<PortfolioRealm>?) : HomeListItem()