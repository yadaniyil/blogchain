package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.objectbox.CoinEntity_
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.data.db.models.objectbox.PortfolioCoinEntity_
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppDbHelper(private val coinBox: Box<CoinEntity>,
                  private val portfolioCoinBox: Box<PortfolioCoinEntity>) : DbHelper {

    override fun getAllCoinsFromDb(): List<CoinEntity> = coinBox.all

    override fun getAllCoinsFromDbLiveData(): ObjectBoxLiveData<CoinEntity> =
            ObjectBoxLiveData(coinBox.query().order(CoinEntity_.rank).build())

    override fun getAllCoinsFiltered(text: String): List<CoinEntity> = coinBox.query()
            .contains(CoinEntity_.symbol, text)
            .or()
            .contains(CoinEntity_.name, text)
            .order(CoinEntity_.rank)
            .build().find()

    override fun getAllCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity> =
            ObjectBoxLiveData(coinBox.query()
                    .contains(CoinEntity_.symbol, text)
                    .or()
                    .contains(CoinEntity_.name, text)
                    .order(CoinEntity_.rank)
                    .build())

    override fun getAllCoinsSorted(fieldName: String, isDescending: Boolean): List<CoinEntity> =
            if (isDescending) coinBox.query().order(CoinEntity_.name).build().find()
            else coinBox.query().orderDesc(CoinEntity_.name).build().find()


    override fun getAllCoinsSortedLiveData(fieldName: String, isDescending: Boolean)
            : ObjectBoxLiveData<CoinEntity> = ObjectBoxLiveData(
            if (isDescending) coinBox.query().order(CoinEntity_.name).build()
            else coinBox.query().orderDesc(CoinEntity_.name).build())

    override fun saveCoinsToDb(coins: List<CoinEntity>) = coinBox.put(coins)

    override fun getCoinFromDb(symbol: String): CoinEntity? = coinBox.query()
            .equal(CoinEntity_.symbol, symbol).build().findFirst()

    override fun addCoinToFavourite(coin: CoinEntity) {
        coin.isFavourite = true
        coinBox.put(coin)
    }

    override fun removeCoinFromFavourites(coin: CoinEntity) {
        coin.isFavourite = false
        coinBox.put(coin)
    }

    override fun getAllFavouriteCoins(): List<CoinEntity> =
            coinBox.query().equal(CoinEntity_.isFavourite, true).build().find()

    override fun getAllFavouriteCoinsLiveData(): ObjectBoxLiveData<CoinEntity> =
            ObjectBoxLiveData(coinBox.query().equal(CoinEntity_.isFavourite, true).build())

    override fun getFavouriteCoinsFiltered(text: String): List<CoinEntity> =
            coinBox.query()
                    .equal(CoinEntity_.isFavourite, true)
                    .and()
                    .contains(CoinEntity_.symbol, text).or().contains(CoinEntity_.name, text)
                    .build().find()

    override fun getFavouriteCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity> =
            ObjectBoxLiveData(coinBox.query()
                    .equal(CoinEntity_.isFavourite, true)
                    .and()
                    .contains(CoinEntity_.symbol, text).or().contains(CoinEntity_.name, text)
                    .build())

    override fun savePortfolioCoin(portfolioCoinEntity: PortfolioCoinEntity) {
        portfolioCoinBox.put(portfolioCoinEntity)
    }

    override fun getAllPortfolioCoins(): List<PortfolioCoinEntity> = portfolioCoinBox.all

    override fun getAllPortfolioCoinsLiveData(): ObjectBoxLiveData<PortfolioCoinEntity> =
            ObjectBoxLiveData(portfolioCoinBox.query().build())

    override fun getSinglePortfolioCoin(portfolioCoinEntityId: Long): PortfolioCoinEntity? =
            portfolioCoinBox.query().equal(PortfolioCoinEntity_.__ID_PROPERTY,
                    portfolioCoinEntityId).build().findFirst()

    override fun removePortfolioCoin(portfolioCoinEntityId: Long) {
        portfolioCoinBox.query().equal(PortfolioCoinEntity_.__ID_PROPERTY,
                portfolioCoinEntityId).build().remove()
    }
}