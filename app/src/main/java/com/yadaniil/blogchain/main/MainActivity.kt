package com.yadaniil.blogchain.main

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.base.BaseActivity
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.no_items_layout.*
import org.jetbrains.anko.onClick
import com.google.android.gms.ads.InterstitialAd
import com.yadaniil.blogchain.base.CurrencyClickListener
import com.yadaniil.blogchain.utils.CurrencyListHelper
import org.jetbrains.anko.toast
import timber.log.Timber


class MainActivity : BaseActivity(), MainView, CurrencyClickListener {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var sortMenuItem: MenuItem? = null

    private lateinit var listDivider: RecyclerView.ItemDecoration
    private lateinit var currenciesAdapter: CurrenciesAdapter
    private lateinit var interstitialAd: InterstitialAd

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        initAdMobBanner()
        initAdMobInterstitial()
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        setUpCurrenciesList(presenter.getRealmCurrencies())
        initSearchView()
        initBackgroundRefresh()
        initRetryRefreshButton()
        initSwipeRefresh()
        presenter.showChangelogDialog()
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
            CoinSorter.showCoinSortDialog(this, currenciesAdapter,
                    {colorSortButtonToWhite()}, {colorSortButtonToAccent()})
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        currencies_recycler_view.adapter = null
    }
    // endregion Activity

    private fun initAdMobBanner() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        adView.loadAd(builder)
    }

    private fun initAdMobInterstitial() {
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.dont_touch_interstitial)
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                interstitialAd.loadAd(builder)
            }
        }
        interstitialAd.loadAd(builder)
    }

    override fun showInterstitialAd() {
        if(interstitialAd.isLoaded)
            interstitialAd.show()
        else
            Timber.e("The interstitial wasn't loaded yet.")
    }

    private fun initBackgroundRefresh() {
//        val scheduledExecutorService = Executors.newScheduledThreadPool(5)
//        scheduledExecutorService.scheduleAtFixedRate({
        presenter.downloadAndSaveAllCurrencies()
//        }, 0, 40, TimeUnit.SECONDS)
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setUpCurrenciesList(presenter.getRealmCurrenciesFiltered(newText ?: ""))
                return true
            }
        })
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

    private fun setUpCurrenciesList(realmCurrencies: RealmResults<CoinMarketCapCurrencyRealm>) {
        currenciesAdapter = CurrenciesAdapter(this, presenter, this)
        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = currenciesAdapter
        currencies_recycler_view.setHasFixedSize(true)
        currencies_recycler_view.setItemViewCacheSize(20)
        currencies_recycler_view.isDrawingCacheEnabled = true
        currencies_recycler_view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        currencies_recycler_view.removeItemDecoration(listDivider)
        currencies_recycler_view.addItemDecoration(listDivider)
        currenciesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (currenciesAdapter.itemCount > 0) {
                    no_items_layout.visibility = View.GONE
                    swipe_refresh.visibility = View.VISIBLE
                    no_items_filtered_layout.visibility = View.GONE
                } else {
                    if(search_view.isSearchOpen) {
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
        })
        currenciesAdapter.setData(realmCurrencies)

        CoinSorter.sortCurrencies(currenciesAdapter)
    }

    private fun colorSortButtonToWhite() {
        if(sortMenuItem != null)
            sortMenuItem?.icon = resources.getDrawable(R.drawable.ic_sort_white_24dp)
    }

    private fun colorSortButtonToAccent() {
        if(sortMenuItem != null)
            sortMenuItem?.icon = resources.getDrawable(R.drawable.ic_sort_accent_24dp)
    }

    // region View
    override fun getLayout() = R.layout.activity_main
    override fun showToolbarLoading() = smooth_progress_bar.progressiveStart()
    override fun stopToolbarLoading() = smooth_progress_bar.progressiveStop()
    override fun updateList() = setUpCurrenciesList(presenter.getRealmCurrencies())
    override fun showChangelogDialog() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val prev = fm.findFragmentByTag("changelogdialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ChangelogDialog().show(ft, "changelogdialog")
    }
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
        if(swipe_refresh.isRefreshing)
            swipe_refresh.isRefreshing = false
    }

    override fun onClick(holder: CurrencyListHelper.CurrencyViewHolder, currencyRealm: CoinMarketCapCurrencyRealm) {
        presenter.addCurrencyToFavourite(currencyRealm)
    }

    override fun onCurrencyAddedToFavourite(currency: CoinMarketCapCurrencyRealm) =
            toast("${currency.symbol} ${getString(R.string.is_now_in_watchlist)}")
    // endregion View
}

