package com.yadaniil.blogchain.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

/**
 * Created by danielyakovlev on 9/19/17.
 */


object AmountFormatter {

    private fun buildDecimalFormatter(maxZerosCount: Int, minZerosCount: Int): NumberFormat {
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        symbols.groupingSeparator = ' '
        val df = DecimalFormat()
        df.roundingMode = RoundingMode.DOWN
        df.decimalFormatSymbols = symbols
        df.maximumFractionDigits = maxZerosCount
        df.minimumFractionDigits = minZerosCount
        df.isGroupingUsed = true
        return df
    }

    fun format(amount: BigDecimal): String {
        return buildDecimalFormatter(2, 2).format(amount)
    }

    fun format(amount: String): String {
        return if(BigDecimal(amount) > BigDecimal(1))
            buildDecimalFormatter(2, 2).format(BigDecimal(amount))
        else
            buildDecimalFormatter(4, 2).format(BigDecimal(amount))
    }
}