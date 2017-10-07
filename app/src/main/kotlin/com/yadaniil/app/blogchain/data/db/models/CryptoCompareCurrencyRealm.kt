package com.yadaniil.app.blogchain.data.db.models

import com.yadaniil.app.blogchain.data.api.models.CryptoCompareCurrenciesListResponse
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by danielyakovlev on 7/2/17.
 */
open class CryptoCompareCurrencyRealm(
        @PrimaryKey var id: Long = 0L,
        var url: String? = "",
        var imageUrl: String? = "",
        var name: String? = "",
        var coinName: String? = "",
        var fullName: String? = "",
        var algorithm: String? = "",
        var proofType: String? = "",
        var fullyPremined: String? = "",
        var totalCoinSupply: String? = "",
        var preminedValue: String? = "",
        var totalCoinsFreeFloat: String? = "",
        var sortOrder: Long = 0L,
        var iconBytes: ByteArray? = null) : RealmObject() {


    companion object {
        fun convertApiResponseToRealmList(response: CryptoCompareCurrenciesListResponse)
                : List<CryptoCompareCurrencyRealm> {
            val currenciesRealmList: MutableList<CryptoCompareCurrencyRealm> = ArrayList()
            val responseData = response.data ?: emptyMap()
            for(data in responseData) {
                val currency = data.value
                currenciesRealmList.add(CryptoCompareCurrencyRealm(id = currency.id, url = currency.url,
                        imageUrl = currency.imageUrl, name = currency.name, coinName = currency.coinName,
                        fullName = currency.fullName, algorithm = currency.algorithm,
                        proofType = currency.proofType, fullyPremined = currency.fullyPremined,
                        totalCoinSupply = currency.totalCoinSupply, preminedValue = currency.preminedValue,
                        totalCoinsFreeFloat = currency.totalCoinsFreeFloat, sortOrder = currency.sortOrder))
            }
            return currenciesRealmList
        }
    }
}