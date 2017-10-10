package com.yadaniil.app.blogchain.data.db

import com.yadaniil.app.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    fun getAllCoinMarketCapCurrenciesFromDb(): RealmResults<CoinMarketCapCurrencyRealm>
    fun getAllCoinMarketCapCurrenciesFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
    fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>)

    fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm>
    fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>)
    fun saveCryptoCompareCurrencyIcon(currency: CoinMarketCapCurrencyRealm, byteArray: ByteArray)
}