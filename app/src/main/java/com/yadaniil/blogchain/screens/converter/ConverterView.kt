package com.yadaniil.blogchain.screens.converter

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.TickerResponse

/**
 * Created by danielyakovlev on 11/15/17.
 */

interface ConverterView : MvpView {
    fun proceedCryptToAnyConversion(ticker: TickerResponse)
    fun proceedFiatToCryptConversion(ticker: TickerResponse)
    fun startToolbarLoading()
    fun stopToolbarLoading()
    fun disableAmountFields()
    fun enableAmountFields()
}