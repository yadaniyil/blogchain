package com.yadaniil.blogchain.screens.portfolio.addcoin

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.CurrencyHelper
import com.yadaniil.blogchain.utils.CurrencyHelper.getSymbolFromFullName
import com.yadaniil.blogchain.utils.Endpoints
import com.yadaniil.blogchain.utils.UiHelper
import kotlinx.android.synthetic.main.activity_add_to_portfolio.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

/**
 * Created by danielyakovlev on 11/3/17.
 */

class AddToPortfolioActivity : MvpAppCompatActivity(), AddToPortfolioView {

    @InjectPresenter lateinit var presenter: AddToPortfolioPresenter
    private var portfolioToEdit: PortfolioRealm? = null
    private var removePortfolioMenuItem: MenuItem? = null

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_portfolio)

        val portfolioId = intent.extras.getString("id")
        portfolioToEdit = presenter.getSinglePortfolio(portfolioId)
        initToolbar()
        presenter.showCoins()
        UiHelper.addCryptocurrencyInputFilter(amount_edit_text)
        UiHelper.addFiatInputFilter(buy_price_edit_text)
        initStorageType()
        initStorageName()
        fillPortfolioIfEditing()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_check, menu)
        removePortfolioMenuItem = menu?.findItem(R.id.action_remove_portfolio)!!
        if(portfolioToEdit == null)
            removePortfolioMenuItem?.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_save_portfolio -> {
            if (amount_edit_text.text.isBlank() || amount_edit_text.text.toString() == "0"
                    || amount_edit_text.text.toString() == ".") {
                toast(R.string.amount_of_coins_should_not_be_empty)
            } else {
                presenter.addCoinToPortfolio(
                        CurrencyHelper.getSymbolFromFullName(coin_spinner.selectedItem.toString()),
                        amount_edit_text.text.toString(),
                        buy_price_edit_text.text.toString(),
                        storage_type_spinner.selectedItem.toString(),
                        storage_name_edit_text.text.toString(),
                        portfolioToEdit,
                        description_edit_text.text.toString())
                finish()
            }
            true
        }
        R.id.action_remove_portfolio -> {
            alert(R.string.remove_from_portfolio_question) {
                yesButton {
                    presenter.removeItemFromPortfolio(portfolioToEdit?.id)
                    finish()
                }
                cancelButton()
            }.show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    // endregion Activity

    // region Init
    private fun initStorageName() {
        storage_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0 || position == 1)
                    storage_type_name_label.text = getString(R.string.wallet_name)
                else if (position == 2) {
                    storage_type_name_label.text = getString(R.string.exchange_name)
                }
            }
        }
    }

    private fun initStorageType() {
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.storage_types))
        storage_type_spinner.adapter = adapter
    }

    private fun initToolbar() {
        if (portfolioToEdit == null) {
            toolbar.title = getString(R.string.add)
        } else {
            toolbar.title = getString(R.string.edit)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    // endregion Init

    private fun fillPortfolioIfEditing() {
        if (portfolioToEdit == null) return
        amount_edit_text.setText(portfolioToEdit?.amountOfCoins)
        buy_price_edit_text.setText(portfolioToEdit?.buyPriceInFiat)
        storage_name_edit_text.setText(portfolioToEdit?.storageName)
        description_edit_text.setText(portfolioToEdit?.description)

        // Select item in storage type spinner
        (0 until storage_type_spinner.count).forEach {
            if (storage_type_spinner.getItemAtPosition(it).toString() == portfolioToEdit?.storageType) {
                storage_type_spinner.setSelection(it)
                return@forEach
            }
        }

        // The item selection for coin spinner is located in showCoin method
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

        // select item in coin spinner
        if (portfolioToEdit != null) {
            (0 until coin_spinner.count).forEach {
                if (coin_spinner.getItemAtPosition(it).toString() ==
                        "${portfolioToEdit?.coin?.name} (${portfolioToEdit?.coin?.symbol})") {
                    coin_spinner.setSelection(it)
                    return@forEach
                }
            }
        }
    }

    private fun showCoinIcon() {
        coin_icon.onClick {
            coin_spinner.dispatchTouchEvent(
                    MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP,
                            0f, 0f, 0))
        }

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