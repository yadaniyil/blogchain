package com.yadaniil.blogchain.screens.findcurrency.crypto

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/15/17.
 */

@InjectViewState
class FindCoinPresenter : MvpPresenter<FindCoinView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

    fun getAllCoins() = repo.getAllCoinsFromDb()

    fun getAllCoinsFiltered(text: String) = repo.getAllCoinsFiltered(text)

    fun getCcCoins() = repo.getAllCryptoCompareCoinsFromDb()
}
