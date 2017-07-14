package com.yadaniil.app.cryptomarket.data.db

import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppDbHelper : DbHelper {

    @Inject lateinit var realm: Realm

    init {
        Application.component?.inject(this)
    }

    override fun getAllCoinMarketCapCurrenciesFromDb(): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java).findAllSorted("sortOrder")

    override fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>) {
        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            realm.where(CryptoCompareCurrencyRealm::class.java).findAllSortedAsync("sortOrder")


    override fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>) {
        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun saveCryptoCompareCurrencyIcon(currency: CryptoCompareCurrencyRealm, byteArray: ByteArray) {
        realm.executeTransactionAsync { currency.iconBytes = byteArray }
    }
}