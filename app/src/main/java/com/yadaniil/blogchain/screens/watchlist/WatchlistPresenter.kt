package com.yadaniil.blogchain.screens.watchlist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/1/17.
 */

@InjectViewState
class WatchlistPresenter : MvpPresenter<WatchlistView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getRealmCurrenciesFavourite(): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllFavouriteCoins()

    fun getAllRealmCurrencies(): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinsFromDb()

    fun getCcRealmCurrencies(): RealmResults<CryptoCompareCurrencyRealm>
            = repo.getAllCryptoCompareCoinsFromDb()

    fun addCoinToFavourite(symbol: String?) {
        val coin = repo.getCoinFromDb(symbol ?: "")
        repo.addCoinToFavourite(coin)
    }

    fun downloadAndSaveAllCurrencies() {
        repo.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showToolbarLoading()
                    viewState.hideSwipeRefreshLoading() }
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinsToDb(currenciesList)
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

    fun removeCoinFromFavourites(currencyRealm: CoinMarketCapCurrencyRealm) {
        repo.removeCoinFromFavourites(currencyRealm)
    }
}