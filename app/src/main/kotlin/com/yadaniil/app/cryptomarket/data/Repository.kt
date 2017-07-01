package com.yadaniil.app.cryptomarket.data

import com.yadaniil.app.cryptomarket.data.api.AppApiHelper
import com.yadaniil.app.cryptomarket.data.api.RetrofitService
import com.yadaniil.app.cryptomarket.data.api.models.TickerResponse
import com.yadaniil.app.cryptomarket.data.db.AppDbHelper
import com.yadaniil.app.cryptomarket.data.db.DbHelper
import com.yadaniil.app.cryptomarket.data.db.models.CurrencyRealm
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
    : RetrofitService, DbHelper, SharedPrefsHelper {

    // region Db
    override fun getAllCurrenciesFromDb(): RealmResults<CurrencyRealm> {
        return appDbHelper.getAllCurrenciesFromDb()
    }

    override fun saveCurrenciesToDb(currencies: List<CurrencyRealm>) {
        appDbHelper.saveCurrenciesToDb(currencies)
    }
    // endregion Db

    // region Api
    override fun getAllCurrencies(convertToCurrency: String?, limit: String?): Observable<List<TickerResponse>> {
        return appApiHelper.getAllCurrencies(convertToCurrency, limit)
    }
    // endregion Api

}