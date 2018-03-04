package com.yadaniil.blogchain.data.api.models.coindar

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 1/8/18.
 */

class CoindarEventResponse(
        @SerializedName("caption") val caption: String,
        @SerializedName("proof") val proof: String,
        @SerializedName("caption_ru") val captionRu: String,
        @SerializedName("proof_ru") val proofRu: String,
        @SerializedName("public_date") val publicDate: String,
        @SerializedName("start_date") val startDate: String,
        @SerializedName("end_date") val endDate: String,
        @SerializedName("coin_name") val coinName: String,
        @SerializedName("coin_symbol") val coinSymbol: String)