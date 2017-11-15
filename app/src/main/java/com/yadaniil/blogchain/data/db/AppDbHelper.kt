package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import io.realm.*
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppDbHelper : DbHelper {

    @Inject lateinit var realm: Realm

    init {
        Application.component?.inject(this)
    }

    // region Coins
    override fun getAllCoinsFromDb(): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java).findAllSortedAsync("rank")

    override fun getCoinFromDb(symbol: String): CoinMarketCapCurrencyRealm? =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("symbol", symbol, Case.INSENSITIVE).findFirst()

    override fun getAllCoinsFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .beginGroup()
                    .contains("name", text, Case.INSENSITIVE)
                    .or()
                    .contains("symbol", text, Case.INSENSITIVE)
                    .endGroup()
                    .findAllSortedAsync("rank")

    override fun getAllCoinsSorted(fieldName: String, sortOrder: Sort): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java).findAllSortedAsync(fieldName, sortOrder)

    override fun saveCoinsToDb(coins: List<CoinMarketCapCurrencyRealm>) {
        // For saving isFavourite currency
        for(currency in coins) {
            val oldCopy = realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("id", currency.id).findFirst()
            if (oldCopy != null) {
                currency.isFavourite = oldCopy.isFavourite
            }
        }

        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(coins) }
    }

    override fun addCoinToFavourite(coin: CoinMarketCapCurrencyRealm) {
        realm.executeTransaction { coin.isFavourite = true }
    }

    override fun removeCoinFromFavourites(coin: CoinMarketCapCurrencyRealm) {
        realm.executeTransaction { coin.isFavourite = false }
    }

    override fun getAllFavouriteCoins(): RealmResults<CoinMarketCapCurrencyRealm> =
            realm.where(CoinMarketCapCurrencyRealm::class.java)
                    .equalTo("isFavourite", true).findAllAsync()

    // endregion Coins

    // region CryptoCompare
    override fun getAllCryptoCompareCoinsFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            realm.where(CryptoCompareCurrencyRealm::class.java).findAllSortedAsync("sortOrder")

    override fun saveCryptoCompareCoinsToDb(coins: List<CryptoCompareCurrencyRealm>) {
        realm.executeTransactionAsync { realm -> realm.copyToRealmOrUpdate(coins) }
    }

    override fun saveCryptoCompareCoinIcon(coin: CoinMarketCapCurrencyRealm, byteArray: ByteArray) {
        realm.executeTransactionAsync { coin.iconBytes = byteArray }
    }
    // endregion CryptoCompare

    // region Portfolio
    override fun addCoinToPortfolio(coin: CoinMarketCapCurrencyRealm, amountOfCoins: String,
                                    buyPriceOfCoin: String, storageType: String, storageName: String, description: String) {
        realm.executeTransaction { realm ->
            val portfolioItem = PortfolioRealm(amountOfCoins = amountOfCoins,
                    buyPriceInFiat = buyPriceOfCoin,
                    storageType = storageType,
                    storageName = storageName,
                    coin = coin,
                    description = description)
            realm.copyToRealmOrUpdate(portfolioItem)
        }
    }

    override fun getAllPortfolio(): RealmResults<PortfolioRealm> {
        return realm.where(PortfolioRealm::class.java).findAllAsync()
    }

    override fun getSinglePortfolio(portfolioId: String): PortfolioRealm? =
        realm.where(PortfolioRealm::class.java).equalTo("id", portfolioId).findFirst()

    override fun editPortfolio(portfolioItem: PortfolioRealm, coin: CoinMarketCapCurrencyRealm,
                               amountOfCoins: String, buyPriceOfCoin: String,
                               storageType: String, storageName: String, description: String) {
        realm.executeTransaction { realm ->
            portfolioItem.coin = coin
            portfolioItem.amountOfCoins = amountOfCoins
            portfolioItem.buyPriceInFiat = buyPriceOfCoin
            portfolioItem.storageType = storageType
            portfolioItem.storageName = storageName
            portfolioItem.description = description
        }
    }

    override fun removeItemFromPortfolio(id: String) {
        realm.executeTransaction { realm ->
            realm.where(PortfolioRealm::class.java)
                    .equalTo("id", id).findFirst().deleteFromRealm()
        }
    }

    // endregion Portfolio
}