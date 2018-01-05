package com.yadaniil.blogchain.screens.allcoins

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.DateHelper
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.no_items_layout.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.util.*


class AllCoinsActivity : BaseActivity(), AllCoinsView, CoinClickListener, CoinLongClickListener {

    @InjectPresenter
    lateinit var presenter: AllCoinsPresenter

    private var sortMenuItem: MenuItem? = null

    private lateinit var listDivider: RecyclerView.ItemDecoration
    private lateinit var allCoinsAdapter: AllCoinsAdapter

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        initSearchView()
        initRetryRefreshButton()
        initSwipeRefresh()
        AllCoinsHelper.coins = presenter.getAllCoinsFromDb()
        AllCoinsHelper.presenter = presenter
        initCurrenciesList(AllCoinsHelper.coins)
        initLastUpdateTime()
        presenter.downloadAndSaveAllCurrencies()
        initAdMobBanner()
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_currencies_list, menu)
        sortMenuItem = menu?.findItem(R.id.action_sort)
        val searchItem = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(searchItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_sort) {
            AllCoinsHelper.showCoinSortDialog(
                    this,
                    allCoinsAdapter,
                    { colorSortButtonToWhite() },
                    { colorSortButtonToAccent() },
                    { updateList(AllCoinsHelper.coins) })
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        currencies_recycler_view.adapter = null
    }
    // endregion Activity

    // region Init
    private fun initLastUpdateTime() {
        presenter.updateLastCoinsUpdateTime()
        val minute: Long = 60 * 1000
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    presenter.updateLastCoinsUpdateTime()
                }
            }
        }, 0L, minute)
    }

    private fun initAdMobBanner() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        adView.loadAd(builder)
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                updateList(presenter.getAllCoinsFiltered(newText ?: ""))

                return true
            }
        })
    }

    private fun initNoItemsLayout(items: RealmResults<CoinMarketCapCurrencyRealm>) {
        items.addChangeListener { coins ->
            if (coins!!.isNotEmpty()) {
                no_items_layout.visibility = View.GONE
                swipe_refresh.visibility = View.VISIBLE
                no_items_filtered_layout.visibility = View.GONE
            } else {
                if (search_view.isSearchOpen) {
                    no_items_layout.visibility = View.GONE
                    swipe_refresh.visibility = View.GONE
                    no_items_filtered_layout.visibility = View.VISIBLE
                } else {
                    no_items_filtered_layout.visibility = View.GONE
                    no_items_layout.visibility = View.VISIBLE
                    swipe_refresh.visibility = View.GONE
                }
            }
        }
    }

    private fun initRetryRefreshButton() {
        retry_button.onClick {
            presenter.downloadAndSaveAllCurrencies()
        }
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            presenter.downloadAndSaveAllCurrencies()
        }
    }

    private fun initCurrenciesList(allCoins: RealmResults<CoinMarketCapCurrencyRealm>) {
        allCoinsAdapter = AllCoinsAdapter(allCoins, true, this,
                presenter.repo, this, this)

        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = allCoinsAdapter
        currencies_recycler_view.itemAnimator = null
        currencies_recycler_view.setHasFixedSize(true)
        currencies_recycler_view.setItemViewCacheSize(200)
        currencies_recycler_view.isDrawingCacheEnabled = true
        currencies_recycler_view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_AUTO
        currencies_recycler_view.removeItemDecoration(listDivider)
        currencies_recycler_view.addItemDecoration(listDivider)
        initNoItemsLayout(allCoins)
    }
    // endregion Init

    // region View
    override fun getLayout() = R.layout.activity_main

    override fun showToolbarLoading() = smooth_progress_bar.progressiveStart()
    override fun stopToolbarLoading() = smooth_progress_bar.progressiveStop()
    override fun showLoadingError() {
        downloading_label.visibility = View.GONE
        progress_bar.visibility = View.GONE

        error_image.visibility = View.VISIBLE
        error_message.visibility = View.VISIBLE
        retry_button.visibility = View.VISIBLE
    }

    override fun showLoading() {
        downloading_label.visibility = View.VISIBLE
        progress_bar.visibility = View.VISIBLE

        error_image.visibility = View.GONE
        error_message.visibility = View.GONE
        retry_button.visibility = View.GONE
    }

    override fun hideSwipeRefreshLoading() {
        if (swipe_refresh.isRefreshing)
            swipe_refresh.isRefreshing = false
    }

    override fun onClick(holder: ListHelper.CoinViewHolder, currencyRealm: CoinMarketCapCurrencyRealm) {
        Navigator.toWebViewActivity("https://coinmarketcap.com/currencies/" + currencyRealm.id + "/",
                currencyRealm.name ?: "", this)
    }

    override fun onLongClick(holder: ListHelper.CoinViewHolder, currencyRealm: CoinMarketCapCurrencyRealm) {

    }

    override fun onCurrencyAddedToFavourite(currency: CoinMarketCapCurrencyRealm) =
            toast("${currency.symbol} ${getString(R.string.is_now_in_watchlist)}")

    override fun showLastCoinsUpdateTime(lastCoinsUpdateTime: Long) {
        val text = if (lastCoinsUpdateTime == 0L)
            "${getString(R.string.last_update)}: ${getString(R.string.never)}"
        else
            "${getString(R.string.last_update)}: ${DateHelper.getTimeAgo(Date(lastCoinsUpdateTime), this)}"

        last_update_time.text = text
    }

    private fun updateList(coins: RealmResults<CoinMarketCapCurrencyRealm>) {
        initNoItemsLayout(coins)
        allCoinsAdapter.updateData(coins)
    }

    private fun colorSortButtonToWhite() {
        if (sortMenuItem != null)
            sortMenuItem?.icon = resources.getDrawable(R.drawable.ic_sort_white_24dp)
    }

    private fun colorSortButtonToAccent() {
        if (sortMenuItem != null)
            sortMenuItem?.icon = resources.getDrawable(R.drawable.ic_sort_accent_24dp)
    }
    // endregion View
}

