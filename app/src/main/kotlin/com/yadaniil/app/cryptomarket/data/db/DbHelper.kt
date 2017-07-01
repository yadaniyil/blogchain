package com.yadaniil.app.cryptomarket.data.db

import com.yadaniil.app.cryptomarket.data.db.models.CurrencyRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    fun getAllCurrenciesFromDb(): RealmResults<CurrencyRealm>

    fun saveCurrenciesToDb(currencies: List<CurrencyRealm>)
}