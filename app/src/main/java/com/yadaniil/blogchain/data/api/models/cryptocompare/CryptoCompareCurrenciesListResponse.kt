package com.yadaniil.blogchain.data.api.models.cryptocompare

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 7/2/17.
 */
class CryptoCompareCurrenciesListResponse(
        @SerializedName("Response") val response: String?,
        @SerializedName("Message") val message: String?,
        @SerializedName("BaseImageUrl") val baseImageUrl: String?,
        @SerializedName("BaseLinkUrl") val baseLinkUrl: String?,
        @SerializedName("Type") val type: Long?,
        @SerializedName("Data") val data: Map<String, Currency>?) {

    class Currency(
            @SerializedName("Id") val id: Long,
            @SerializedName("Url") val url: String,
            @SerializedName("ImageUrl") val imageUrl: String,
            @SerializedName("Name") val name: String,
            @SerializedName("Symbol") val symbol: String,
            @SerializedName("CoinName") val coinName: String,
            @SerializedName("FullName") val fullName: String,
            @SerializedName("Algorithm") val algorithm: String,
            @SerializedName("ProofType") val proofType: String,
            @SerializedName("FullyPremined") val fullyPremined: String,
            @SerializedName("TotalCoinSupply") val totalCoinSupply: String,
            @SerializedName("PreMinedValue") val preminedValue: String,
            @SerializedName("TotalCoinsFreeFloat") val totalCoinsFreeFloat: String,
            @SerializedName("SortOrder") val sortOrder: Long)

}




