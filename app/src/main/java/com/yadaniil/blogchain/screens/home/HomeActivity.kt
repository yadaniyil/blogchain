package com.yadaniil.blogchain.screens.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import io.reactivex.rxkotlin.toObservable
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/2/17.
 */

class HomeActivity : BaseActivity(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    private lateinit var homeAdapter: HomeAdapter

    override fun getLayout() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstFourCoins: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
        presenter.getAllCoins().toObservable().subscribe({
            firstFourCoins.add(it)
        }, {
            Timber.e(it.message)
        }, {
            initHomeView(firstFourCoins)
        })

        presenter.showChangelogDialog()
        presenter.downloadAndSaveAllCurrencies()
    }

    private fun initHomeView(firstFourCoins: List<CoinMarketCapCurrencyRealm>) {
        homeAdapter = HomeAdapter(mutableListOf(
                PortfolioItem("2345.03 USD"),
                CoinsItem(firstFourCoins),
                NewsItem("Bitcoin is SCAM!!!"),
                NewsItem("Bitcoin is GOD!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!"),
                NewsItem("Bitcoin is just cryptocurrency!!!")
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

    private fun updateAll() {

    }
}