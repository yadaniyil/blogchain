package com.yadaniil.blogchain.screens.portfolio

import android.widget.TextView
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.ListHelper
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 12/6/17.
 */

object PortfolioHelper {

    fun updateTotalFiatBalance(portfolios: List<PortfolioCoinEntity>?,
                               fiatAmount: TextView, btcAmount: TextView) {
        if(portfolios == null || portfolios.isEmpty()) {
            fiatAmount.text = "0 USD"
            btcAmount.text = "0 BTC"
        } else {
            var sumFiat: BigDecimal = BigDecimal.ZERO
            var sumBtc: BigDecimal = BigDecimal.ZERO
            portfolios.forEach {
                sumFiat += ListHelper.calculatePortfolioFiatSum(it)
                sumBtc += ListHelper.calculatePortfolioBtcSum(it)
            }

            fiatAmount.text = "${AmountFormatter.formatFiatPrice(sumFiat)} USD"
            btcAmount.text = "${AmountFormatter.formatCryptoPrice(sumBtc.toString())} BTC"
        }
    }

    fun updateTotalFiatBalance(portfolios: List<PortfolioCoinEntity>?,
                               amount: TextView) {
        if(portfolios == null || portfolios.isEmpty()) {
            amount.text = "0 USD (0 BTC)"
        } else {
            var sumFiat: BigDecimal = BigDecimal.ZERO
            var sumBtc: BigDecimal = BigDecimal.ZERO
            portfolios.forEach {
                sumFiat += ListHelper.calculatePortfolioFiatSum(it)
                sumBtc += ListHelper.calculatePortfolioBtcSum(it)
            }

            val fiatBalance = AmountFormatter.formatFiatPrice(sumFiat)
            val btcBalance = AmountFormatter.formatCryptoPrice(sumBtc.toString())
            amount.text = "$$fiatBalance ($btcBalance BTC)"
        }
    }
}