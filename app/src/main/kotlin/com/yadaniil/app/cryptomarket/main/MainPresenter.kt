package com.yadaniil.app.cryptomarket.main

import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import timber.log.Timber
import javax.inject.Inject


class MainPresenter @Inject constructor(private val view: IMainView) {

    @Inject lateinit var repo: Repository

//    fun getRealmCurrencies(): RealmResults<CoinMarketCapCurrencyRealm> = repo.getAllCoinMarketCapCurrenciesFromDb()
    fun getRealmCurrencies(): RealmResults<CryptoCompareCurrencyRealm> = repo.getAllCryptoCompareCurrenciesFromDb()

//    fun downloadAndSaveAllCurrencies() {
//        repo.getAllCurrencies()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { view.showLoading() }
//                .doOnTerminate { view.stopLoading() }
//                .subscribe {
//                    currenciesList ->
//                    repo.saveCoinMarketCapCurrenciesToDb(
//                            CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(currenciesList))
//                }
//    }

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.stopLoading() }
                .subscribe({
                    currenciesList ->
                    repo.saveCryptoCompareCurrenciesToDb(currenciesList)
                }, {
                    error ->
                    Timber.e(error.message)
                })
    }

    fun saveCryptoCompareCurrencyIcon(currencyRealm: CryptoCompareCurrencyRealm, byteArray: ByteArray)
            = repo.saveCryptoCompareCurrencyIcon(currencyRealm, byteArray)

}