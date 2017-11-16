package com.yadaniil.blogchain.screens.findcurrency

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.screens.findcurrency.crypto.FindCoinFragment
import com.yadaniil.blogchain.screens.findcurrency.favourite.FindFavouriteFragment
import com.yadaniil.blogchain.screens.findcurrency.fiat.FindFiatFragment

/**
 * Created by danielyakovlev on 11/15/17.
 */


class FindCurrencyPagerAdapter(fm: FragmentManager, private val search_view: MaterialSearchView)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment? = when (i) {
        0 -> FindFiatFragment.newInstance(search_view)
        1 -> FindCoinFragment.newInstance(search_view)
        2 -> FindFavouriteFragment.newInstance(search_view)
        else -> FindCoinFragment.newInstance(search_view)
    }

    override fun getCount() = 3

    companion object {
        val PAGE_FIAT = 0
        val PAGE_ALL_COINS = 1
    }
}