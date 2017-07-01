package com.yadaniil.app.cryptomarket.data.db

import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.db.models.CurrencyRealm
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppDbHelper : DbHelper {

    @Inject lateinit var realm: Realm

    init { Application.component?.inject(this) }

    override fun getAllCurrenciesFromDb(): RealmResults<CurrencyRealm> {
        return realm.where(CurrencyRealm::class.java).findAll()
    }

    override fun saveCurrenciesToDb(currencies: List<CurrencyRealm>) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(currencies) }
    }
}