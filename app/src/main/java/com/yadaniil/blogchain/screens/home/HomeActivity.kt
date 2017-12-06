package com.yadaniil.blogchain.screens.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by danielyakovlev on 11/2/17.
 */

class HomeActivity : BaseActivity(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    private lateinit var portfolios: RealmResults<PortfolioRealm>
    private lateinit var coins: RealmResults<CoinMarketCapCurrencyRealm>

    private lateinit var homeAdapter: HomeAdapter

    override fun getLayout() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coins = presenter.getAllCoins()
        portfolios = presenter.getPortfolios()

        initHomeView()
        initTotalPortfolioBalance()
        initCoins()

        swipe_refresh.setOnRefreshListener {
            presenter.downloadAndSaveAllCurrencies()
        }
        presenter.showChangelogDialog()
        presenter.downloadAndSaveAllCurrencies()
    }

    private fun initTotalPortfolioBalance() {
        portfolios.addChangeListener { portfolios ->
            homeAdapter.updatePortfolio(portfolios)
        }
    }

    private fun initCoins() {
        coins.addChangeListener { coins ->
            homeAdapter.updateCoins(coins)
        }
    }

    private fun initHomeView() {
        homeAdapter = HomeAdapter(mutableListOf(
                PortfolioSection(portfolios),
                CoinsSection(coins),
                NewsSection(presenter.downloadNews())
        ), this, presenter)
        home_recycler_view.layoutManager = LinearLayoutManager(this)
        home_recycler_view.adapter = homeAdapter
        home_recycler_view.setHasFixedSize(true)
    }

    override fun showChangelogDialog() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val prev = fm.findFragmentByTag("changelogdialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ChangelogDialog().show(ft, "changelogdialog")
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun stopLoading() {
        swipe_refresh.isRefreshing = false
    }
}