package com.yadaniil.blogchain.screens.findcurrency

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.screens.findcurrency.crypto.FindCoinFragment
import com.yadaniil.blogchain.screens.findcurrency.favourite.FindFavouriteFragment
import com.yadaniil.blogchain.screens.findcurrency.fiat.FindFiatFragment
import com.yadaniil.blogchain.screens.watchlist.WatchlistActivity

/**
 * Created by danielyakovlev on 11/15/17.
 */


class FindCurrencyPagerAdapter(fm: FragmentManager, private val search_view: MaterialSearchView,
                               private val findPurposeRequestCode: Int)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment? {
        if(findPurposeRequestCode == WatchlistActivity.PICK_FAVOURITE_COIN_REQUEST_CODE)
            return FindCoinFragment.newInstance(search_view, initSearchImmediately = true)
        else return when (i) {
            0 -> FindFiatFragment.newInstance(search_view)
            1 -> FindCoinFragment.newInstance(search_view, true)
            2 -> FindFavouriteFragment.newInstance(search_view)
            else -> FindCoinFragment.newInstance(search_view, true)
        }
    }

    override fun getCount() = if(findPurposeRequestCode == WatchlistActivity.PICK_FAVOURITE_COIN_REQUEST_CODE) 1 else 3

}