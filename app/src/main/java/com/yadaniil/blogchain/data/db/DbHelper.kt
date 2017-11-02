package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    fun getAllCoinMarketCapCurrenciesFromDb(): RealmResults<CoinMarketCapCurrencyRealm>
    fun getAllCoinMarketCapCurrenciesFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
    fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>)
    fun getCMCCurrencyFromDb(symbol: String): CoinMarketCapCurrencyRealm

    fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm>
    fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>)
    fun saveCryptoCompareCurrencyIcon(currency: CoinMarketCapCurrencyRealm, byteArray: ByteArray)

    fun addCurrencyToFavourite(currency: CoinMarketCapCurrencyRealm)
    fun getAllFavouriteCurrencies(): RealmResults<CoinMarketCapCurrencyRealm>
}