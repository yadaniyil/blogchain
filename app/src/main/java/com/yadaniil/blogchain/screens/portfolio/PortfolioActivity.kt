package com.yadaniil.blogchain.screens.portfolio

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_portfolio.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast


/**
 * Created by danielyakovlev on 11/3/17.
 */

class PortfolioActivity : BaseActivity(), PortfolioView, PortfolioAdapter.OnClick, PortfolioAdapter.OnLongClick {

    @InjectPresenter
    lateinit var presenter: PortfolioPresenter

    private var portfolios: RealmResults<PortfolioRealm>? = null

    private lateinit var portfolioAdapter: PortfolioAdapter
    private lateinit var listDivider: RecyclerView.ItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        fab.onClick { Navigator.toAddCoinToPortfolioActivity(this) }

        initTotalPortfolioBalance()
        initPortfolioList()
        initSwipeRefresh()
        presenter.downloadAndSaveAllCurrencies()

    }

    private fun initTotalPortfolioBalance() {
        portfolios = presenter.getPortfolios()
        portfolios?.addChangeListener { portfolios ->
            PortfolioHelper.updateTotalFiatBalance(portfolios, total_amount, total_amount_btc)
            if(portfolios.isEmpty()) {
                no_items_text_view.visibility = View.VISIBLE
                swipe_refresh.visibility = View.GONE
            } else {
                no_items_text_view.visibility = View.GONE
                swipe_refresh.visibility = View.VISIBLE
            }

        }
    }

    private fun initSwipeRefresh() {
        swipe_refresh.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            presenter.downloadAndSaveAllCurrencies()
        }
    }

    private fun initPortfolioList() {
        portfolioAdapter = PortfolioAdapter(portfolios!!, true,
                this, presenter.repo, this, this)
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

    override fun showToolbarLoading() = smooth_progress_bar.progressiveStart()
    override fun stopToolbarLoading() = smooth_progress_bar.progressiveStop()

    override fun showLoadingError() = toast(R.string.error)

    override fun hideSwipeRefreshLoading() {
        if(swipe_refresh.isRefreshing)
            swipe_refresh.isRefreshing = false
    }

    override fun onLongClick(holder: ListHelper.PortfolioViewHolder, portfolioItem: PortfolioRealm) =
            Navigator.toAddCoinToPortfolioActivity(this, portfolioItem.id)

    override fun onClick(holder: ListHelper.PortfolioViewHolder, portfolioItem: PortfolioRealm) {

    }
    // endregion View

}