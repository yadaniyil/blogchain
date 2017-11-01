package com.yadaniil.blogchain.watchlist

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
            = repo.getAllFavouriteCurrencies()

    fun getCcRealmCurrencies(): RealmResults<CryptoCompareCurrencyRealm>
            = repo.getAllCryptoCompareCurrenciesFromDb()

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showToolbarLoading()
                    viewState.hideSwipeRefreshLoading() }
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCurrenciesToDb(currenciesList)
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }
}