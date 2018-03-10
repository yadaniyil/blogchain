package com.yadaniil.blogchain.screens.portfolio

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import kotlinx.android.synthetic.main.activity_portfolio.*
import kotlinx.android.synthetic.main.layout_portfolio_balance.*
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel


/**
 * Created by danielyakovlev on 11/3/17.
 */

class PortfolioActivity : BaseActivity(), PortfolioAdapter.OnClick, PortfolioAdapter.OnLongClick {

    private val viewModel by viewModel<PortfolioViewModel>()

    private lateinit var portfolioAdapter: PortfolioAdapter
    private lateinit var listDivider: RecyclerView.ItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        fab.onClick { Navigator.toAddCoinToPortfolioActivity(this, 0) }

        initTotalPortfolioBalance()
        initPortfolioList()
        initSwipeRefresh()
        viewModel.downloadAndSaveAllCurrencies()

    }

    private fun initTotalPortfolioBalance() {
        viewModel.getPortfoliosLiveData().observe(this, Observer {
            it?.let {
                PortfolioHelper.updateTotalFiatBalance(it, total_amount, total_amount_btc)
                if(it.isEmpty()) {
                    no_items_text_view.visibility = View.VISIBLE
                    swipe_refresh.visibility = View.GONE
                } else {
                    no_items_text_view.visibility = View.GONE
                    swipe_refresh.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            viewModel.downloadAndSaveAllCurrencies()
        }
    }

    private fun initPortfolioList() {
        portfolioAdapter = PortfolioAdapter(this, this, this)
        watchlist_recycler_view.layoutManager = LinearLayoutManager(this)
        watchlist_recycler_view.adapter = portfolioAdapter
        watchlist_recycler_view.itemAnimator = null
        watchlist_recycler_view.setHasFixedSize(true)
        watchlist_recycler_view.removeItemDecoration(listDivider)
        watchlist_recycler_view.addItemDecoration(listDivider)


        watchlist_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fab.hide()
                } else if (dy < 0) {
                    fab.show()
                }
            }
        })
    }

    // region View
    override fun getLayout() = R.layout.activity_portfolio

    fun showToolbarLoading() = smooth_progress_bar.progressiveStart()
    fun stopToolbarLoading() = smooth_progress_bar.progressiveStop()

    fun showLoadingError() = toast(R.string.error)

    fun hideSwipeRefreshLoading() {
        if(swipe_refresh.isRefreshing)
            swipe_refresh.isRefreshing = false
    }

    override fun onLongClick(holder: ListHelper.PortfolioViewHolder, portfolioCoinEntity: PortfolioCoinEntity) =
            Navigator.toAddCoinToPortfolioActivity(this, portfolioCoinEntity.id)

    override fun onClick(holder: ListHelper.PortfolioViewHolder, portfolioCoinEntity: PortfolioCoinEntity) {

    }
    // endregion View

}