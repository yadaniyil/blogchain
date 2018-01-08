package com.yadaniil.blogchain.data.api.models.cryptocompare

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 9/21/17.
 */


class Miner(
        @SerializedName("Id") val id: String,
        @SerializedName("ParentId") val parentId: String,
        @SerializedName("Company") val company: String,
        @SerializedName("Url") val url: String,
        @SerializedName("LogoUrl") val logoUrl: String,
        @SerializedName("Name") val name: String,
        @SerializedName("Recommended") val isRecommended: Boolean,
        @SerializedName("Sponsored") val isSponsored: Boolean,
        @SerializedName("AffiliateURL") val affiliateUrl: String,
        @SerializedName("Algorithm") val algorithm: String,
        @SerializedName("HashesPerSecond") val hashesPerSecond: String,
        @SerializedName("Cost") val cost: String,
        @SerializedName("Currency") val currency: String,
        @SerializedName("EquipmentType") val equipmentType: String,
        @SerializedName("PowerConsumption") val powerConsumption: String,
        @SerializedName("CurrenciesAvailable") val currenciesAvailable: String,
        @SerializedName("CurrenciesAvailableLogo") val currenciesAvailableLogo: String,
        @SerializedName("CurrenciesAvailableName") val currenciesAvailableName: String
)