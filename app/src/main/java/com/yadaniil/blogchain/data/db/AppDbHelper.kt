package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
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

    override fun getCMCCurrencyFromDb(symbol: String): CoinMarketCapCurrencyRealm =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("symbol", symbol, Case.INSENSITIVE).findFirst()

    override fun getAllCoinMarketCapCurrenciesFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .beginGroup()
                    .contains("name", text, Case.INSENSITIVE)
                    .or()
                    .contains("symbol", text, Case.INSENSITIVE)
                    .endGroup()
                    .findAllSortedAsync("rank")

    override fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>) {
        // For saving isFavourite currency
        for(currency in currencies) {
            val oldCopy = realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("id", currency.id).findFirst()
            if (oldCopy != null) {
                currency.isFavourite = oldCopy.isFavourite
            }
        }

        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            realm.where(CryptoCompareCurrencyRealm::class.java).findAllSortedAsync("sortOrder")

    override fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>) {
        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(currencies) }
    }

    override fun saveCryptoCompareCurrencyIcon(currency: CoinMarketCapCurrencyRealm, byteArray: ByteArray) {
        realm.executeTransactionAsync { currency.iconBytes = byteArray }
    }

    override fun addCurrencyToFavourite(currency: CoinMarketCapCurrencyRealm) {
        realm.executeTransaction { currency.isFavourite = true }
    }

    override fun getAllFavouriteCurrencies(): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("isFavourite", true).findAllAsync()

}