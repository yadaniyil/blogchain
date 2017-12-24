package com.yadaniil.blogchain.screens.home

import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 12/5/17.
 */

sealed class HomeListSection
data class CoinsSection(val coin: RealmResults<CoinMarketCapCurrencyRealm>) : HomeListSection()
data class PortfolioSection(val portfolios: RealmResults<PortfolioRealm>?) : HomeListSection()