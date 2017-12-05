package com.yadaniil.blogchain.screens.findcurrency

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.findcurrency.events.InitCoinsSearchViewEvent
import com.yadaniil.blogchain.screens.findcurrency.events.InitFavouritesSearchViewEvent
import com.yadaniil.blogchain.screens.findcurrency.events.InitFiatSearchViewEvent
import com.yadaniil.blogchain.screens.watchlist.WatchlistActivity
import kotlinx.android.synthetic.main.activity_find_coin.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by danielyakovlev on 11/2/17.
 */

class FindCurrencyActivity : MvpAppCompatActivity(), FindCurrencyView {

    @InjectPresenter
    lateinit var presenter: FindCurrencyPresenter
    private var findPurposeRequestCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_coin)
        findPurposeRequestCode = intent.extras.getInt("requestCode")
        initToolbar()
        initViewPager()
    }

    private fun initViewPager() {
        val pagerAdapter = FindCurrencyPagerAdapter(supportFragmentManager, search_view,
                findPurposeRequestCode ?: 0, this)
        pager.adapter = pagerAdapter

        RxViewPager.pageSelections(pager).subscribe {
            when (it) {
                0 -> EventBus.getDefault().post(InitFiatSearchViewEvent("Search through fiat enabled"))
                1 -> EventBus.getDefault().post(InitCoinsSearchViewEvent("Search through all coins enabled"))
                2 -> EventBus.getDefault().post(InitFavouritesSearchViewEvent("Search through favourite coins enabled"))
            }
        }

        tab_layout.setupWithViewPager(pager)

        if (findPurposeRequestCode != WatchlistActivity.PICK_FAVOURITE_COIN_REQUEST_CODE)
            pager.currentItem = 1
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.currencies)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_find, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(searchItem)
        return true
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        val PICKED_COIN_SYMBOL = "picked_coin_symbol"
    }
}