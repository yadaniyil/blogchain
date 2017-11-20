package com.yadaniil.blogchain.screens.converter

import com.arellomobile.mvp.MvpView

/**
 * Created by danielyakovlev on 11/15/17.
 */

interface ConverterView : MvpView {
    fun setConversionValues(topCurrency: String, bottomCurrency: String)
    fun startToolbarLoading()
    fun stopToolbarLoading()
}