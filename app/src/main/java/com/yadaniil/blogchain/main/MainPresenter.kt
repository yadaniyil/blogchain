package com.yadaniil.blogchain.main

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
class MainPresenter : MvpPresenter<IMainView>() {

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
                .observeOn(AndroidSchedulers.mainThread())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .doOnSubscribe { viewState.showLoading() }
                .doOnComplete { downloadCMCList() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCurrenciesToDb(currenciesList)
                }, { error ->
                    Timber.e(error.message)
                })
    }

    private fun downloadCMCList() {
        repo.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .doOnComplete { viewState.stopLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinMarketCapCurrenciesToDb(currenciesList)
                    viewState.updateList()
                }, { error ->
                    Timber.e(error.message)
                })
    }

    fun showChangelogDialog() {
        if(repo.getLastShowChangelogVersion() != BuildConfig.VERSION_CODE) {
            viewState.showChangelogDialog()
            repo.setLastShowChangelogVersion(BuildConfig.VERSION_CODE)
        }
    }

//    private fun downloadAndSavePricesMultiFull() {
//        repo.getPriceMultiFull(buildFromSymbols(), buildToSymbols())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnComplete { viewState.stopLoading() }
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