package com.yadaniil.blogchain.screens.coins

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.BuildConfig
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import timber.log.Timber
import javax.inject.Inject

// CMC - CoinMarketCap

@InjectViewState
class AllCoinsPresenter : MvpPresenter<AllCoinsView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getRealmCurrencies(): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinMarketCapCurrenciesFromDb()

    fun getRealmCurrenciesFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
            = repo.getAllCoinMarketCapCurrenciesFromDbFiltered(text)

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showToolbarLoading()
                    viewState.showLoading(); viewState.hideSwipeRefreshLoading() }
                .doOnComplete { downloadCMCList() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCurrenciesToDb(currenciesList)
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

    private fun downloadCMCList() {
        repo.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinMarketCapCurrenciesToDb(currenciesList)
                    repo.getAllCoinMarketCapCurrenciesFromDb()
                    viewState.updateList()
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

    fun addCurrencyToFavourite(currency: CoinMarketCapCurrencyRealm) {
        repo.addCurrencyToFavourite(currency)
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
//        val currencies = repo.getAllCryptoCompareCurrenciesFromDb().subList(0, 10)
//        var symbols: String = ""
//        for(currency in currencies) {
//            symbols += currency.coinName + ","
//        }
//        return symbols
//    }

//    private fun buildToSymbols() = "BTC,USD"

    fun saveCurrencyIcon(currencyRealm: CoinMarketCapCurrencyRealm, byteArray: ByteArray)
            = repo.saveCryptoCompareCurrencyIcon(currencyRealm, byteArray)

}