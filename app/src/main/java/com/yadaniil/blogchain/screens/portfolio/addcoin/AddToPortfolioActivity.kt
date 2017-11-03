package com.yadaniil.blogchain.screens.portfolio.addcoin

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.CurrencyHelper.getSymbolFromFullName
import com.yadaniil.blogchain.utils.Endpoints
import com.yadaniil.blogchain.utils.UiHelper
import kotlinx.android.synthetic.main.activity_add_to_portfolio.*

/**
 * Created by danielyakovlev on 11/3/17.
 */

class AddToPortfolioActivity : MvpAppCompatActivity(), AddToPortfolioView {

    @InjectPresenter lateinit var presenter: AddToPortfolioPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_portfolio)
        initToolbar()
        presenter.showCoins()
        UiHelper.addCryptocurrencyInputFilter(amount_edit_text)
        UiHelper.addFiatInputFilter(buy_price_edit_text)
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.add_to_portfolio)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_done -> {
                presenter.addCoinToPortfolio(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showCoin(coins: List<CoinMarketCapCurrencyRealm>) {
        val coinsForDisplay = coins.map { "${it.name} (${it.symbol})" }
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, coinsForDisplay)
        coin_spinner.setTitle(getString(R.string.select_coin))
        coin_spinner.setPositiveButton(getString(R.string.ok))
        coin_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                showCoinIcon()
                coin_symbol.text = getSymbolFromFullName(coin_spinner.selectedItem.toString())
            }
        }
        coin_spinner.adapter = adapter
    }

    private fun showCoinIcon() {
        val imageLink = presenter.getLinkForCoinImage(coin_spinner.selectedItem.toString())
        if (imageLink.isBlank()) {
            coin_icon.setImageResource(R.drawable.icon_ico)
        } else {
            Picasso.with(this)
                    .load(Uri.parse(Endpoints.CRYPTO_COMPARE_URL + imageLink))
                    .into(coin_icon)
        }
    }
}