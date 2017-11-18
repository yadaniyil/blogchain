package com.yadaniil.blogchain.utils

import com.google.gson.Gson
import com.yadaniil.blogchain.data.api.models.TickerResponse
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by danielyakovlev on 11/18/17.
 */

object TickerParser {

    fun parseTickerResponse(response: String) {
        val jsonArray = JSONArray(response)
        val jsonObject = jsonArray.get(0) as JSONObject

        val map = HashMap<String, String>()
        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next() as String
            val value = jsonObject.getString(key)
            map.put(key, value)
        }

        var priceFiatAnalogue = ""
        var dayVolumeFiatAnalogue = ""
        var marketCapFiatAnalogue = ""

        for ((key, value) in map) {
            if (key.startsWith("price_") && !key.endsWith("btc") && !key.endsWith("usd"))
                priceFiatAnalogue = value

            if (key.startsWith("24h_volume_") && !key.endsWith("usd"))
                dayVolumeFiatAnalogue = value

            if (key.startsWith("market_cap_") && !key.endsWith("usd"))
                marketCapFiatAnalogue = value

        }

        val ticker = Gson().fromJson<TickerResponse>(jsonObject.toString(), TickerResponse::class.java)
        ticker.priceFiatAnalogue = priceFiatAnalogue
        ticker.dayVolumeFiatAnalogue = dayVolumeFiatAnalogue
        ticker.marketCapFiatAnalogue = marketCapFiatAnalogue
    }
}