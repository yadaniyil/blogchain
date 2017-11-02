package com.yadaniil.blogchain.watchlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.base.BaseActivity
import com.yadaniil.blogchain.base.CurrencyClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.findcoin.FindCoinActivity
import com.yadaniil.blogchain.utils.CurrencyListHelper
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_watchlist.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast


/**
 * Created by danielyakovlev on 10/31/17.
 */

class WatchlistActivity : BaseActivity(), WatchlistView, CurrencyClickListener {

    @InjectPresenter
    lateinit var presenter: WatchlistPresenter
    private val PICK_FAVOURITE_COIN_REQUEST_CODE = 0

    private lateinit var watchlistAdapter: WatchlistAdapter
    private lateinit var listDivider: RecyclerView.ItemDecoration

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        initAdMobBanner()
        initToolbar()
        initSwipeRefresh()
        initFab()
        setUpWatchlist(presenter.getRealmCurrenciesFavourite())
        presenter.downloadAndSaveAllCurrencies()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_FAVOURITE_COIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                presenter.addCoinToFavourite(data?.extras?.getString(FindCoinActivity.PICKED_COIN_SYMBOL))
            }
        }
    }
    // endregion Activity

    // region Init
    private fun initFab() {
        fab.onClick { startActivityForResult(
                Intent(this, FindCoinActivity::class.java),
                PICK_FAVOURITE_COIN_REQUEST_CODE) }
    }

    private fun initToolbar() {
//        toolbar.title = getString(R.string.drawer_item_watchlist)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initAdMobBanner() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        adView.loadAd(builder)
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            presenter.downloadAndSaveAllCurrencies()
        }
    }

    private fun setUpWatchlist(realmCurrencies: RealmResults<CoinMarketCapCurrencyRealm>) {
        watchlistAdapter = WatchlistAdapter(realmCurrencies, true, this,
                presenter.getCcRealmCurrencies(), this)
        watchlist_recycler_view.layoutManager = LinearLayoutManager(this)
        watchlist_recycler_view.adapter = watchlistAdapter
        watchlist_recycler_view.setHasFixedSize(true)
        watchlist_recycler_view.removeItemDecoration(listDivider)
        watchlist_recycler_view.addItemDecoration(listDivider)
    }
    // endregion Init

    // region View
    override fun showToolbarLoading() = smooth_progress_bar.progressiveStart()
    override fun stopToolbarLoading() = smooth_progress_bar.progressiveStop()

    override fun showLoadingError() = toast(R.string.error)

    override fun hideSwipeRefreshLoading() {
        if(swipe_refresh.isRefreshing)
            swipe_refresh.isRefreshing = false
    }

    override fun onClick(holder: CurrencyListHelper.CurrencyViewHolder, currencyRealm: CoinMarketCapCurrencyRealm) {

    }

    override fun getLayout() = R.layout.activity_watchlist
    // endregion View
}