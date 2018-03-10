package com.yadaniil.blogchain.screens.allcoins

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.DateHelper
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import com.yadaniil.blogchain.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_with_search_and_update_time.*
import kotlinx.android.synthetic.main.loading_error_layout.*
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.progress_bar_loading_layout.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.listeners.onClick
import org.koin.android.architecture.ext.viewModel
import java.util.*

// TODO Search view
// TODO reactive last update
// TODO Sorting
// TODO Fix updating coins. PAC remained on third rank after cmc shifted it down
class AllCoinsActivity : BaseActivity(), CoinClickListener, CoinLongClickListener {

    private val viewModel by viewModel<AllCoinsViewModel>()
    private var sortMenuItem: MenuItem? = null

    private lateinit var allCoinsAdapter: AllCoinsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchView()
        initRetryRefreshButton()
        initSwipeRefresh()
        initCurrenciesList()
        initLastUpdateTime()

        observeViewState()
        observeSnackbar()
        viewModel.downloadAndSaveAllCurrencies(runSwipeToRefresh = true)
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
            // TODO
//            AllCoinsHelper.showCoinSortDialog(
//                    this,
//                    allCoinsAdapter,
//                    { colorSortButtonToWhite() },
//                    { colorSortButtonToAccent() },
//                    { observeAllCoins(AllCoinsHelper.coins) })
        }

        return super.onOptionsItemSelected(item)
    }

    // region Init
    private fun initLastUpdateTime() {
        val minute: Long = 60 * 1000
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    showLastCoinsUpdateTime(viewModel.getLastCoinsUpdateTime())
                }
            }
        }, 0L, minute)
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                observeCoins(newText ?: "")
                return true
            }
        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                observeCoins()
            }

            override fun onSearchViewShown() {
                observeCoins()
            }
        })
    }

    private fun initRetryRefreshButton() {
        retry_button.onClick {
            viewModel.downloadAndSaveAllCurrencies()
        }
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            viewModel.downloadAndSaveAllCurrencies()
        }
    }

    private fun initCurrenciesList() {
        allCoinsAdapter = AllCoinsAdapter(this, this, this)
        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = allCoinsAdapter
        currencies_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        if (AllCoinsHelper.coins.isNotEmpty()) allCoinsAdapter.setCoins(AllCoinsHelper.coins)
    }
    // endregion Init

    // region View
    override fun getLayout() = R.layout.activity_main

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer {
            it?.let {
                when (it) {
                    is ProgressBarLoadingViewState -> showProgressBarLoading()
                    is LoadingErrorViewState -> showLoadingError()
                    is SwipeRefreshLoadingViewState -> showSwipeRefreshLoading()
                    is StopSwipeRefreshLoadingViewState -> stopSwipeRefreshLoading()
                    is CoinsShowingViewState -> showCoinsList()
                }
            }
        })
    }

    private fun observeSnackbar() {
        viewModel.snackbarMessage.observe(this, Observer {
            it?.let {
                when (it) {
                    R.string.update_error -> {
                        longSnackbar(find(R.id.all_coins_container), it, R.string.retry, {
                            viewModel.downloadAndSaveAllCurrencies(runSwipeToRefresh = true)
                        }).show()
                    }
                }
            }
        })
    }

    private fun observeCoins(searchQuery: String? = null) {
        viewModel.getCoinsLiveData(searchQuery).removeObservers(this)
        viewModel.getCoinsLiveData(searchQuery).observe(this, Observer {
            it?.let { allCoinsAdapter.setCoins(it) }
        })
    }

    private fun showProgressBarLoading() {
        stopSwipeRefreshLoading()
        swipe_refresh.visible = false
        no_items_filtered_layout.visible = false
        loading_error_layout.visible = false

        progress_bar_loading_layout.visible = true
    }

    private fun showLoadingError() {
        stopSwipeRefreshLoading()
        swipe_refresh.visible = false
        progress_bar_loading_layout.visible = false
        no_items_filtered_layout.visible = false

        loading_error_layout.visible = true
    }

    private fun showSwipeRefreshLoading() {
        progress_bar_loading_layout.visible = false
        no_items_filtered_layout.visible = false
        loading_error_layout.visible = false

        swipe_refresh.visible = true
        swipe_refresh.isRefreshing = true
    }

    private fun showCoinsList() {
        progress_bar_loading_layout.visible = false
        no_items_filtered_layout.visible = false
        loading_error_layout.visible = false

        swipe_refresh.visible = true
    }

    private fun stopSwipeRefreshLoading() {
        // 200 ms delay is used to escape swipe to refresh lags due to huge list update (1500+ items)
        if (swipe_refresh.isRefreshing)
            Handler().postDelayed({ swipe_refresh.isRefreshing = false }, 200)
    }

    override fun onClick(holder: ListHelper.CoinViewHolder, coinEntity: CoinEntity) {
        Navigator.toWebViewActivity("https://coinmarketcap.com/currencies/"
                + coinEntity.cmcId + "/", coinEntity.name, this)
    }

    override fun onLongClick(holder: ListHelper.CoinViewHolder, coinEntity: CoinEntity) {

    }

    fun showLastCoinsUpdateTime(lastCoinsUpdateTime: Long) {
        val text = if (lastCoinsUpdateTime == 0L)
            "${getString(R.string.last_update)}: ${getString(R.string.never)}"
        else
            "${getString(R.string.last_update)}: ${DateHelper.getTimeAgo(Date(lastCoinsUpdateTime), this)}"

        last_update_time.text = text
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

