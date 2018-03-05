package com.yadaniil.blogchain.screens.findcurrency.fiat

import android.support.annotation.DrawableRes

/**
 * Created by danielyakovlev on 11/16/17.
 */

sealed class FiatListItem
class FiatHeaderItem(var text: String) : FiatListItem()
class FiatCurrencyItem(var symbol: String, var name: String, @DrawableRes var iconIntRes: Int) : FiatListItem()