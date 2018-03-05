package com.yadaniil.blogchain.utils

import android.content.Context
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.findcurrency.fiat.FiatCurrencyItem

/**
 * Created by danielyakovlev on 11/17/17.
 */

object FiatCurrenciesHelper {

    fun getAll(context: Context): List<FiatCurrencyItem> {
        val symbols = context.resources.getStringArray(R.array.fiat_currencies_symbols)
        val names = context.resources.getStringArray(R.array.fiat_currencies_names)
        return listOf(
                FiatCurrencyItem(symbols[0], names[0], R.drawable.usd),
                FiatCurrencyItem(symbols[1], names[1], R.drawable.eur),
                FiatCurrencyItem(symbols[2], names[2], R.drawable.gbp),
                FiatCurrencyItem(symbols[3], names[3], R.drawable.jpy),
                FiatCurrencyItem(symbols[4], names[4], R.drawable.rub),
                FiatCurrencyItem(symbols[5], names[5], R.drawable.aud),
                FiatCurrencyItem(symbols[6], names[6], R.drawable.brl),
                FiatCurrencyItem(symbols[7], names[7], R.drawable.cad),
                FiatCurrencyItem(symbols[8], names[8], R.drawable.chf),
                FiatCurrencyItem(symbols[9], names[9], R.drawable.clp),
                FiatCurrencyItem(symbols[10], names[10], R.drawable.cny),
                FiatCurrencyItem(symbols[11], names[11], R.drawable.czk),
                FiatCurrencyItem(symbols[12], names[12], R.drawable.dkk),
                FiatCurrencyItem(symbols[13], names[13], R.drawable.hkd),
                FiatCurrencyItem(symbols[14], names[14], R.drawable.huf),
                FiatCurrencyItem(symbols[15], names[15], R.drawable.idr),
                FiatCurrencyItem(symbols[16], names[16], R.drawable.ils),
                FiatCurrencyItem(symbols[17], names[17], R.drawable.inr),
                FiatCurrencyItem(symbols[18], names[18], R.drawable.krw),
                FiatCurrencyItem(symbols[19], names[19], R.drawable.mxn),
                FiatCurrencyItem(symbols[20], names[20], R.drawable.myr),
                FiatCurrencyItem(symbols[21], names[21], R.drawable.nok),
                FiatCurrencyItem(symbols[22], names[22], R.drawable.nzd),
                FiatCurrencyItem(symbols[23], names[23], R.drawable.php),
                FiatCurrencyItem(symbols[24], names[24], R.drawable.pkr),
                FiatCurrencyItem(symbols[25], names[25], R.drawable.pln),
                FiatCurrencyItem(symbols[26], names[26], R.drawable.sek),
                FiatCurrencyItem(symbols[27], names[27], R.drawable.sgd),
                FiatCurrencyItem(symbols[28], names[28], R.drawable.thb),
                FiatCurrencyItem(symbols[29], names[29], R.drawable.try_flag),
                FiatCurrencyItem(symbols[30], names[30], R.drawable.twd),
                FiatCurrencyItem(symbols[31], names[31], R.drawable.zar)
        )
    }

    fun isFiat(symbol: String, allFiats: List<FiatCurrencyItem>) = allFiats.any { it.symbol == symbol }
}