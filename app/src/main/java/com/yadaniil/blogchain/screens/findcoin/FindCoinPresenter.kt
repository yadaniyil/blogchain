package com.yadaniil.blogchain.screens.findcoin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/2/17.
 */

@InjectViewState
class FindCoinPresenter : MvpPresenter<FindCoinView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getAllRealmCurrencies(): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinMarketCapCoinsFromDb()

    fun getRealmCurrenciesFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinMarketCapCoinsFromDbFiltered(text)

}