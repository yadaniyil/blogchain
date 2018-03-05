package com.yadaniil.blogchain.screens.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.portfolio.PortfolioHelper
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.Navigator
import com.yadaniil.blogchain.utils.charts.MarketCapChart
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_market_info.*
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel


/**
 * Created by danielyakovlev on 11/2/17.
 */

class HomeActivity : BaseActivity() {

    private val viewModel by viewModel<HomeViewModel>()

    private lateinit var portfolios: List<PortfolioCoinEntity>
    private lateinit var coins: List<CoinEntity>

    override fun getLayout() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coins = viewModel.getAllCoins()
        portfolios = viewModel.getPortfolios()

        initTotalPortfolioBalance()
//        initCoins()
        initGlobalDataAndPortfolio()
        initMarketCapChart()

        swipe_refresh.setOnRefreshListener {
            viewModel.updateAll()
        }
        viewModel.showChangelogDialog()
        viewModel.updateAll()
    }

    private fun initGlobalDataAndPortfolio() {
        viewModel.setSavedGlobalData()
        viewModel.showOrHidePortfolioBalance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_hide_portfolio) {
            viewModel.showOrHidePortfolioBalance(portfolios_balance_layout.visibility == View.VISIBLE)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initTotalPortfolioBalance() {
        updatePortfolio(portfolios)
    }

    private fun initMarketCapChart() {
        viewModel.setSavedGlobalDataChart()
    }

//    private fun initCoins() {
//        updateCoins(coins)
//        coins.addChangeListener { coins ->
//            updateCoins(coins)
//        }
//    }

//    private fun updateCoins(coins: RealmResults<CoinEntity>) {
//        ImageLoader.loadCoinIcon(coins[0].symbol ?: "", first_coin_icon,
//                this, viewModel.repo)
//        ImageLoader.loadCoinIcon(coins[1].symbol ?: "", second_coin_icon,
//                this, viewModel.repo)
//        ImageLoader.loadCoinIcon(coins[2].symbol ?: "", third_coin_icon,
//                this, viewModel.repo)
//        ImageLoader.loadCoinIcon(coins[3].symbol ?: "", forth_coin_icon,
//                this, viewModel.repo)
//
//        first_coin_name.text = coins[0].name
//        first_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[0].priceUsd.toString())}"
//        first_coin_layout.onClick { openCoinPage(coins[0]) }
//
//        second_coin_name.text = coins[1].name
//        second_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[1].priceUsd.toString())}"
//        second_coin_layout.onClick { openCoinPage(coins[1]) }
//
//        third_coin_name.text = coins[2].name
//        third_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[2].priceUsd.toString())}"
//        third_coin_layout.onClick { openCoinPage(coins[2]) }
//
//        forth_coin_name.text = coins[3].name
//        forth_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[3].priceUsd.toString())}"
//        forth_coin_layout.onClick { openCoinPage(coins[3]) }
//    }

    private fun openCoinPage(coinEntity: CoinEntity) {
        Navigator.toWebViewActivity("https://coinmarketcap.com/currencies/"
                + coinEntity.cmcId + "/", coinEntity.name, this)
    }

    private fun updatePortfolio(portfolios: List<PortfolioCoinEntity>) =
            PortfolioHelper.updateTotalFiatBalance(portfolios, portfolios_balance)

    fun showChangelogDialog() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val prev = fm.findFragmentByTag("changelogdialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ChangelogDialog().show(ft, "changelogdialog")
    }

    fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    fun stopLoading() {
        swipe_refresh.isRefreshing = false
    }

    fun showLoadingError() = toast(R.string.error)

    fun updateGlobalData(globalData: CmcGlobalDataResponse?) {
        if(globalData == null) {
            market_cap.text = "?"
            daily_volume.text = "?"
            btc_dominance.text = "?"
            return
        }
        val marketCapText = "$${AmountFormatter.formatFiatPrice(globalData.totalMarketCapUsd.toString())}"
        market_cap.text = marketCapText

        val dailyVolumeText = "$${AmountFormatter.formatFiatPrice(globalData.total24hVolumeUsd.toString())}"
        daily_volume.text = dailyVolumeText

        val btcDominanceText = "${globalData.bitcoinDominance}%"
        btc_dominance.text = btcDominanceText
    }

    fun showOrHidePortfolio(toShow: Boolean) {
        if(toShow)
            portfolios_balance_layout.visibility = View.VISIBLE
        else
            portfolios_balance_layout.visibility = View.GONE
    }

    fun updateMarketCapChart(data: CmcMarketCapAndVolumeChartResponse?) {
        if(data?.marketCaps == null) {
            return
        }

        MarketCapChart.initMarketCapChart(chart, data, this)
    }
}