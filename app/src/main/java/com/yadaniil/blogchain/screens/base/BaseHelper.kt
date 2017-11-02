package com.yadaniil.blogchain.screens.base

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem

/**
 * Created by danielyakovlev on 11/2/17.
 */
object BaseHelper {

    var selectedDrawerItem: Long = 1

    val DRAWER_ITEM_HOME_ID: Long = 1
    val DRAWER_ITEM_MARKET_INFO_ID: Long = 2
    val DRAWER_ITEM_CONVERTER_ID: Long = 3
    val DRAWER_ITEM_WATCHLIST_ID: Long = 4
    val DRAWER_ITEM_PORTFOLIO_ID: Long = 5
    val DRAWER_ITEM_EXCHANGES_ID: Long = 6
    val DRAWER_ITEM_ICO_ID: Long = 7
    val DRAWER_ITEM_MINING_ID: Long = 8
    val DRAWER_ITEM_SETTINGS_ID: Long = 9
    val DRAWER_ITEM_AD_ID: Long = 10

    fun primaryItem(id: Long, @StringRes name: Int, @DrawableRes icon: Int, enabled: Boolean = true,
                    onClick: () -> Unit ): PrimaryDrawerItem {

        return PrimaryDrawerItem().withIdentifier(id).withEnabled(enabled).withName(name)
                .withIcon(icon).withOnDrawerItemClickListener { _, _, _->
            if(selectedDrawerItem != id) onClick(); false }
    }




}