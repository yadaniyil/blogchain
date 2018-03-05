package com.yadaniil.blogchain.screens.watchlist

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/1/17.
 */

class WatchlistViewModel(private val repo: Repository) : ViewModel() {

    fun getFavouriteCoinsLiveData() = repo.getAllFavouriteCoinsLiveData()

    fun addCoinToFavourite(symbol: String?) {
        val coin = repo.getCoinFromDb(symbol ?: "")
        repo.addCoinToFavourite(coin!!)
    }

    fun getFavouriteCoinsFiltered(filter: String) = repo.getFavouriteCoinsFiltered(filter)

    fun downloadAndSaveAllCurrencies() {
        repo.getAllCoins(limit = "0")
                .subscribeOn(Schedulers.io())
//                .map { CoinEntity.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    viewState.showToolbarLoading()
//                    viewState.hideSwipeRefreshLoading() }
//                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
//                    repo.saveCoinsToDb(currenciesList)
                }, { error ->
//                    viewState.showLoadingError()
//                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

    fun removeCoinFromFavourites(currencyRealm: CoinEntity) {
        repo.removeCoinFromFavourites(currencyRealm)
    }
}