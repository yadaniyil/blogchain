package com.yadaniil.blogchain.screens.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.portfolio.PortfolioHelper
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.ImageLoader
import com.yadaniil.blogchain.utils.Navigator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_cards_coins.*
import kotlinx.android.synthetic.main.layout_market_info.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

/**
 * Created by danielyakovlev on 11/2/17.
 */

class HomeActivity : BaseActivity(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    private lateinit var portfolios: RealmResults<PortfolioRealm>
    private lateinit var coins: RealmResults<CoinMarketCapCurrencyRealm>

    override fun getLayout() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coins = presenter.getAllCoins()
        portfolios = presenter.getPortfolios()

        initTotalPortfolioBalance()
        initCoins()
        initGlobalDataAndPortfolio()

        swipe_refresh.setOnRefreshListener {
            presenter.updateAll()
        }
        presenter.showChangelogDialog()
        presenter.updateAll()
    }

    private fun initGlobalDataAndPortfolio() {
//        presenter.setSavedGlobalData()
        presenter.showOrHidePortfolioBalance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_hide_portfolio) {
            presenter.showOrHidePortfolioBalance(portfolios_balance_layout.visibility == View.VISIBLE)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initTotalPortfolioBalance() {
        updatePortfolio(portfolios)
        portfolios.addChangeListener { portfolios ->
            updatePortfolio(portfolios)
        }
    }

    private fun initCoins() {
        updateCoins(coins)
        coins.addChangeListener { coins ->
            updateCoins(coins)
        }
    }

    private fun updateCoins(coins: RealmResults<CoinMarketCapCurrencyRealm>) {
        ImageLoader.loadCoinIcon(coins[0].symbol ?: "", first_coin_icon,
                this, presenter.repo)
        ImageLoader.loadCoinIcon(coins[1].symbol ?: "", second_coin_icon,
                this, presenter.repo)
        ImageLoader.loadCoinIcon(coins[2].symbol ?: "", third_coin_icon,
                this, presenter.repo)
        ImageLoader.loadCoinIcon(coins[3].symbol ?: "", forth_coin_icon,
                this, presenter.repo)

        first_coin_name.text = coins[0].name
        first_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[0].priceUsd.toString())}"
        first_coin_layout.onClick { openCoinPage(coins[0]) }

        second_coin_name.text = coins[1].name
        second_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[1].priceUsd.toString())}"
        second_coin_layout.onClick { openCoinPage(coins[1]) }

        third_coin_name.text = coins[2].name
        third_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[2].priceUsd.toString())}"
        third_coin_layout.onClick { openCoinPage(coins[2]) }

        forth_coin_name.text = coins[3].name
        forth_coin_price.text = "$${AmountFormatter.formatFiatPrice(coins[3].priceUsd.toString())}"
        forth_coin_layout.onClick { openCoinPage(coins[3]) }
    }

    private fun openCoinPage(currencyRealm: CoinMarketCapCurrencyRealm) {
        Navigator.toWebViewActivity("https://coinmarketcap.com/currencies/" + currencyRealm.id + "/",
                currencyRealm.name ?: "", this)
    }

    private fun updatePortfolio(portfolios: RealmResults<PortfolioRealm>) =
            PortfolioHelper.updateTotalFiatBalance(portfolios, portfolios_balance)

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

    override fun showLoadingError() = toast(R.string.error)

    override fun updateGlobalData(globalData: CmcGlobalDataResponse) {
        val marketCapText = "$${AmountFormatter.formatFiatPrice(globalData.totalMarketCapUsd.toString())}"
        market_cap.text = marketCapText

        val dailyVolumeText = "$${AmountFormatter.formatFiatPrice(globalData.total24hVolumeUsd.toString())}"
        daily_volume.text = dailyVolumeText

        val btcDominanceText = "${globalData.bitcoinDominance}%"
        btc_dominance.text = btcDominanceText
    }

    override fun showOrHidePortfolio(toShow: Boolean) {
        if(toShow)
            portfolios_balance_layout.visibility = View.VISIBLE
        else
            portfolios_balance_layout.visibility = View.GONE
    }
}