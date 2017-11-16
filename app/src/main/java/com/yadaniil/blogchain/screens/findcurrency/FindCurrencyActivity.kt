package com.yadaniil.blogchain.screens.findcurrency

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
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
        initViewPager()
        initTabs()
        initToolbar()
    }

    private fun initViewPager() {
        val pagerAdapter = FindCurrencyPagerAdapter(supportFragmentManager, search_view,
                findPurposeRequestCode ?: 0)
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> EventBus.getDefault().post(InitFiatSearchViewEvent("Search through fiat enabled"))
                    1 -> EventBus.getDefault().post(InitCoinsSearchViewEvent("Search through all coins enabled"))
                    2 -> EventBus.getDefault().post(InitFavouritesSearchViewEvent("Search through favourite coins enabled"))
                }
            }
        })
    }

    private fun initTabs() {
        if(findPurposeRequestCode == WatchlistActivity.PICK_FAVOURITE_COIN_REQUEST_CODE)
            tab_layout.addTab(tab_layout.newTab().setText(R.string.coins))
        else {
            tab_layout.addTab(tab_layout.newTab().setText(R.string.fiat))
            tab_layout.addTab(tab_layout.newTab().setText(R.string.coins))
            tab_layout.addTab(tab_layout.newTab().setText(R.string.favourites))
        }


        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
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