package com.yadaniil.blogchain.screens.allcoins

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import io.realm.Sort
import timber.log.Timber
import javax.inject.Inject

// CMC - CoinMarketCap

@InjectViewState
class AllCoinsPresenter : MvpPresenter<AllCoinsView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getAllCoinsFromDb(): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinsFromDb()

    fun getAllCryptoCompareCoinsFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            repo.getAllCryptoCompareCoinsFromDb()

    fun getAllCoinsFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinsFiltered(text)

    fun getAllCoinsSorted(fieldName: String, sortOrder: Sort): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinsSorted(fieldName, sortOrder)

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showToolbarLoading()
                    viewState.showLoading(); viewState.hideSwipeRefreshLoading()
                }
                .doOnComplete { downloadCMCList() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCoinsToDb(currenciesList)
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                }
                )
    }

    private fun downloadCMCList() {
        repo.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinsToDb(currenciesList)
                    repo.getAllCoinsFromDb()
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                }
                )
    }

    fun addCurrencyToFavourite(currency: CoinMarketCapCurrencyRealm) {
        repo.addCoinToFavourite(currency)
        viewState.onCurrencyAddedToFavourite(currency)
    }

//    private fun downloadAndSavePricesMultiFull() {
//        repo.getPriceMultiFull(buildFromSymbols(), buildToSymbols())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnComplete { viewState.stopToolbarLoading() }
//                .subscribe({
//
//                })
//    }

//    private fun buildFromSymbols(): String {
//        val currencies = repo.getAllCryptoCompareCoinsFromDb().subList(0, 10)
//        var symbols: String = ""
//        for(currency in currencies) {
//            symbols += currency.coinName + ","
//        }
//        return symbols
//    }

//    private fun buildToSymbols() = "BTC,USD"

    fun saveCurrencyIcon(currencyRealm: CoinMarketCapCurrencyRealm, byteArray: ByteArray)
            = repo.saveCryptoCompareCoinIcon(currencyRealm, byteArray)

}