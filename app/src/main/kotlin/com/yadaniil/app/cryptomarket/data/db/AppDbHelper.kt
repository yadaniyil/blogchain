package com.yadaniil.app.cryptomarket.data.db

import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import io.realm.Case
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
            realm.where(CoinMarketCapCurrencyRealm::class.java).findAllSortedAsync("rank")

    override fun getAllCoinMarketCapCurrenciesFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .beginGroup()
                    .beginsWith("name", text, Case.INSENSITIVE)
                    .or()
                    .beginsWith("symbol", text, Case.INSENSITIVE)
                    .endGroup()
                    .findAllSortedAsync("rank")

    override fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            realm.where(CryptoCompareCurrencyRealm::class.java).findAllSortedAsync("sortOrder")


    override fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>) {
        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun saveCryptoCompareCurrencyIcon(currency: CoinMarketCapCurrencyRealm, byteArray: ByteArray) {
        realm.executeTransactionAsync { currency.iconBytes = byteArray }
    }
}