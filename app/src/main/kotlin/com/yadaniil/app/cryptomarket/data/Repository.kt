package com.yadaniil.app.cryptomarket.data

import com.yadaniil.app.cryptomarket.data.api.AppApiHelper
import com.yadaniil.app.cryptomarket.data.api.CoinMarketCapService
import com.yadaniil.app.cryptomarket.data.api.CryptoCompareService
import com.yadaniil.app.cryptomarket.data.api.models.CryptoCompareCurrenciesListResponse
import com.yadaniil.app.cryptomarket.data.api.models.TickerResponse
import com.yadaniil.app.cryptomarket.data.db.AppDbHelper
import com.yadaniil.app.cryptomarket.data.db.DbHelper
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.app.cryptomarket.data.prefs.SharedPrefs
import com.yadaniil.app.cryptomarket.data.prefs.SharedPrefsHelper
import io.reactivex.Observable
import io.realm.RealmResults
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by danielyakovlev on 7/1/17.
 */

@Singleton
class Repository @Inject constructor(var appApiHelper: AppApiHelper,
                                     var appDbHelper: AppDbHelper,
                                     var sharedPrefs: SharedPrefs)
    : CoinMarketCapService, CryptoCompareService, DbHelper, SharedPrefsHelper {

    // region Db
    override fun getAllCoinMarketCapCurrenciesFromDb(): RealmResults<CoinMarketCapCurrencyRealm> {
        return appDbHelper.getAllCoinMarketCapCurrenciesFromDb()
    }

    override fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>) {
        appDbHelper.saveCoinMarketCapCurrenciesToDb(currencies)
    }

    override fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm> {
        return appDbHelper.getAllCryptoCompareCurrenciesFromDb()
    }

    override fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>) {
        appDbHelper.saveCryptoCompareCurrenciesToDb(currencies)
    }

    override fun saveCryptoCompareCurrencyIcon(currency: CryptoCompareCurrencyRealm, byteArray: ByteArray) {
        appDbHelper.saveCryptoCompareCurrencyIcon(currency, byteArray)
    }
    // endregion Db

    // region Api
    override fun getAllCurrencies(convertToCurrency: String?, limit: String?): Observable<List<TickerResponse>> {
        return appApiHelper.getAllCurrencies(convertToCurrency, limit)
    }

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> {
        return appApiHelper.getFullCurrenciesList()
    }
    // endregion Api

}