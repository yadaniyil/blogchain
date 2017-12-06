package com.yadaniil.blogchain.screens.portfolio

import android.widget.TextView
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.RealmResults
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 12/6/17.
 */

object PortfolioHelper {

    fun updateTotalFiatBalance(portfolios: RealmResults<PortfolioRealm>?,
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
}