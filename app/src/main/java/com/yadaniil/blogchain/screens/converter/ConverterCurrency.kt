package com.yadaniil.blogchain.screens.converter

/**
 * Created by danielyakovlev on 11/17/17.
 */

sealed class ConverterCurrency(val symbol: String)
data class ConverterCryptoCurrency(private val cryptocurrencySymbol: String, val cmcId: String)
    : ConverterCurrency(cryptocurrencySymbol)
data class ConverterFiatCurrency(private val fiatSymbol: String) : ConverterCurrency(fiatSymbol)