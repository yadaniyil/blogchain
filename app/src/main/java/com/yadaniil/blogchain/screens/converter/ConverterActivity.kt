package com.yadaniil.blogchain.screens.converter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.TickerResponse
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity
import com.yadaniil.blogchain.screens.findcurrency.fiat.listitems.FiatCurrencyItem
import com.yadaniil.blogchain.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_converter.*
import org.jetbrains.anko.*
import timber.log.Timber
import java.math.BigDecimal


/**
 * Created by danielyakovlev on 11/15/17.
 */

class ConverterActivity : BaseActivity(), ConverterView {

    @InjectPresenter
    lateinit var presenter: ConverterPresenter

    private lateinit var allCoins: RealmResults<CoinMarketCapCurrencyRealm>
    private lateinit var allCcCoins: RealmResults<CryptoCompareCurrencyRealm>

    private lateinit var allFiatCurrencies: List<FiatCurrencyItem>

    private lateinit var topCurrency: ConverterCurrency
    private lateinit var bottomCurrency: ConverterCurrency

    private lateinit var topAmountSubscription: Disposable
    private lateinit var bottomAmountSubscription: Disposable

    private lateinit var ticker: TickerResponse
    private lateinit var topTicker: TickerResponse
    private lateinit var bottomTicker: TickerResponse
    private var isTopAmountFocused: Boolean = false
    private var isBottomAmountFocused: Boolean = false

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allCoins = presenter.getAllCoins()
        allCcCoins = presenter.getAllCcCoins()
        allFiatCurrencies = FiatCurrenciesHelper.getAll(this)
        initTopCurrency()
        initBottomCurrency()
        updateTicker()
        swipe_refresh.setOnRefreshListener { updateTicker() }
        initConversionAmounts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_TOP_CONVERT_CURRENCY) {
            if (resultCode == RESULT_OK) {
                val symbol = data?.extras?.getString(FindCurrencyActivity.PICKED_COIN_SYMBOL) ?: ""
                val fiat = allFiatCurrencies.find { it.symbol == symbol }
                if (fiat != null) {
                    initTopCurrency(fiat = fiat)
                } else {
                    val coin = presenter.getCoin(symbol)
                    initTopCurrency(coin = coin)
                }
            }
        } else if (requestCode == PICK_BOTTOM_CONVERT_CURRENCY) {
            if (resultCode == RESULT_OK) {
                val symbol = data?.extras?.getString(FindCurrencyActivity.PICKED_COIN_SYMBOL) ?: ""
                val fiat = allFiatCurrencies.find { it.symbol == symbol }
                if (fiat != null) {
                    initBottomCurrency(fiat = fiat)
                } else {
                    val coin = presenter.getCoin(symbol)
                    initBottomCurrency(coin = coin)
                }
            }
        }

        updateTicker()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!topAmountSubscription.isDisposed) {
            topAmountSubscription.dispose()
        }

        if (!bottomAmountSubscription.isDisposed) {
            bottomAmountSubscription.dispose()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_converter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_swap) {
            swapCurrencies()
        } else if (item?.itemId == R.id.action_clear) {
            top_amount.text.clear()
            bottom_amount.text.clear()
        }
        return super.onOptionsItemSelected(item)
    }
    // endregion Activity

    // region Init
    private fun initConversionAmounts() {
        top_amount.setOnFocusChangeListener { _, isFocused -> isTopAmountFocused = isFocused }
        bottom_amount.setOnFocusChangeListener { _, isFocused -> isBottomAmountFocused = isFocused }

        topAmountSubscription = RxTextView.afterTextChangeEvents(top_amount).skipInitialValue()
                .map { it.editable().toString().replace(" ", "") }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { amount ->
                    if (isTopAmountFocused)
                        performTopToBottomConversion(amount)
                }

        bottomAmountSubscription = RxTextView.afterTextChangeEvents(bottom_amount).skipInitialValue()
                .map { it.editable().toString().replace(" ", "") }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { amount ->
                    if (isBottomAmountFocused)
                        initBottomToTopConversion(amount)
                }
    }

    private fun initTopCurrency(coin: CoinMarketCapCurrencyRealm? = null, fiat: FiatCurrencyItem? = null) {
        val topCurrencySymbol = top_currency.find<TextView>(R.id.currency_symbol)
        val topCurrencyName = top_currency.find<TextView>(R.id.currency_name)
        val topCurrencyIcon = top_currency.find<ImageView>(R.id.currency_icon)

        if (coin == null && fiat == null) {
            // Default is crypto
            topCurrencySymbol.text = allCoins[0].symbol
            topCurrencyName.text = allCoins[0].name
            ListHelper.downloadAndSetIcon(topCurrencyIcon, allCoins[0], presenter.repo, this)
            topCurrency = ConverterCryptoCurrency(allCoins[0].symbol ?: "", allCoins[0]?.id ?: "")
        } else if (coin != null && fiat == null) {
            // Crypto
            topCurrencySymbol.text = coin.symbol
            topCurrencyName.text = coin.name
            ListHelper.downloadAndSetIcon(topCurrencyIcon, coin, presenter.repo, this)
            topCurrency = ConverterCryptoCurrency(coin.symbol ?: "", coin.id ?: "")
        } else if (coin == null && fiat != null) {
            // Fiat
            topCurrencySymbol.text = fiat.symbol
            topCurrencyName.text = fiat.name
            topCurrencyIcon.setImageDrawable(resources.getDrawable(fiat.iconIntRes))
            topCurrency = ConverterFiatCurrency(fiat.symbol)
        }

        top_currency.onClick { Navigator.toFindCurrencyActivity(this, PICK_TOP_CONVERT_CURRENCY) }
    }

    private fun initBottomCurrency(coin: CoinMarketCapCurrencyRealm? = null, fiat: FiatCurrencyItem? = null) {
        val bottomCurrencySymbol = bottom_currency.find<TextView>(R.id.currency_symbol)
        val bottomCurrencyName = bottom_currency.find<TextView>(R.id.currency_name)
        val bottomCurrencyIcon = bottom_currency.find<ImageView>(R.id.currency_icon)

        if (coin == null && fiat == null) {
            // Default is fiat
            bottomCurrencySymbol.text = allFiatCurrencies[0].symbol
            bottomCurrencyName.text = allFiatCurrencies[0].name
            bottomCurrencyIcon.setImageDrawable(resources.getDrawable(allFiatCurrencies[0].iconIntRes))
            bottomCurrency = ConverterFiatCurrency(allFiatCurrencies[0].symbol)
        } else if (coin != null && fiat == null) {
            // Crypto
            bottomCurrencySymbol.text = coin.symbol
            bottomCurrencyName.text = coin.name
            ListHelper.downloadAndSetIcon(bottomCurrencyIcon, coin, presenter.repo, this)
            bottomCurrency = ConverterCryptoCurrency(coin.symbol ?: "", coin?.id ?: "")
        } else if (coin == null && fiat != null) {
            // Fiat
            bottomCurrencySymbol.text = fiat.symbol
            bottomCurrencyName.text = fiat.name
            bottomCurrencyIcon.setImageDrawable(resources.getDrawable(fiat.iconIntRes))
            bottomCurrency = ConverterFiatCurrency(fiat.symbol)
        }

        bottom_currency.onClick { Navigator.toFindCurrencyActivity(this, PICK_BOTTOM_CONVERT_CURRENCY) }
    }
    // endregion Init

    // region Conversion
    private fun swapCurrencies() {
        val bufferCurrency = topCurrency
        topCurrency = bottomCurrency
        bottomCurrency = bufferCurrency

        val topCurrencySymbol = top_currency.find<TextView>(R.id.currency_symbol)
        val topCurrencyName = top_currency.find<TextView>(R.id.currency_name)
        val topCurrencyIcon = top_currency.find<ImageView>(R.id.currency_icon)

        val bottomCurrencySymbol = bottom_currency.find<TextView>(R.id.currency_symbol)
        val bottomCurrencyName = bottom_currency.find<TextView>(R.id.currency_name)
        val bottomCurrencyIcon = bottom_currency.find<ImageView>(R.id.currency_icon)

        val bufferSymbol = topCurrencySymbol.text.toString()
        val bufferName = topCurrencyName.text.toString()
        val bufferIcon = topCurrencyIcon.image

        topCurrencySymbol.text = bottomCurrencySymbol.text.toString()
        topCurrencyName.text = bottomCurrencyName.text.toString()
        topCurrencyIcon.setImageDrawable(bottomCurrencyIcon.image)

        bottomCurrencySymbol.text = bufferSymbol
        bottomCurrencyName.text = bufferName
        bottomCurrencyIcon.setImageDrawable(bufferIcon)


        updateTicker()
    }

    private fun updateTicker() {
        if (topCurrency.isCrypto()) {
            presenter.cryptToAnyConversion(
                    (topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol)
        } else if (topCurrency.isFiat() && bottomCurrency.isCrypto()) {
            presenter.fiatToCryptoConversion(
                    (bottomCurrency as ConverterCryptoCurrency).id, topCurrency.symbol)
        } else if (topCurrency.isFiat() && bottomCurrency.isFiat()) {
            presenter.fiatToFiatConversion(topCurrency.symbol, bottomCurrency.symbol)
        }

        try {
            Timber.d("Top ticker: " + topTicker.symbol)
            Timber.d("Bottom ticker: " + bottomTicker.symbol)
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    private fun calculateConversion(amount: Double, rate: Double): String {
        return AmountFormatter.formatCryptoPrice(BigDecimal(amount * rate))
    }

    private fun ConverterCurrency.isCrypto() = CryptocurrencyHelper.isCrypto(this.symbol, allCoins)
    private fun ConverterCurrency.isFiat() = FiatCurrenciesHelper.isFiat(this.symbol, allFiatCurrencies)

    private fun performTopToBottomConversion(topAmount: String? = null) {
        val amount = topAmount ?: top_amount.text.toString().replace(" ", "")

        if (amount.isEmpty()) {
            bottom_amount.text.clear()
            return
        }

        // Top - fiat, bottom - crypto
        if (topCurrency.isFiat() && bottomCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) 1 / ticker.priceFiatAnalogue.toDouble()
                    else 1 / ticker.priceUsd.toDouble()
            bottom_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo))
        }

        // Top - crypto, bottom - fiat
        if (bottomCurrency.isFiat() && topCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) ticker.priceFiatAnalogue
                    else ticker.priceUsd
            bottom_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo.toDouble()))
        }

        // Both fiat
        if (topCurrency.isFiat() && bottomCurrency.isFiat()) {
            val topPriceFiat =
                    if (topTicker.priceFiatAnalogue.isNotEmpty()) topTicker.priceFiatAnalogue
                    else topTicker.priceUsd

            val bottomPriceFiat =
                    if (bottomTicker.priceFiatAnalogue.isNotEmpty()) bottomTicker.priceFiatAnalogue
                    else bottomTicker.priceUsd

            bottom_amount.setText(calculateConversion(
                    amount.toDouble(), bottomPriceFiat.toDouble() / topPriceFiat.toDouble()))
        }

        // Both crypto
        if (topCurrency.isCrypto() && bottomCurrency.isCrypto()) {
            val priceToConvertTo = if (ticker.priceFiatAnalogue == "")
                ticker.priceBtc
            else
                ticker.priceFiatAnalogue

            bottom_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo.toDouble()))
        }
    }

    private fun initBottomToTopConversion(bottomAmount: String? = null) {
        val amount = bottomAmount ?: bottom_amount.text.toString().replace(" ", "")

        if (amount.isEmpty()) {
            top_amount.text.clear()
            return
        }

        // Top - fiat, bottom - crypto
        if (topCurrency.isFiat() && bottomCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) ticker.priceFiatAnalogue.toDouble()
                    else ticker.priceUsd.toDouble()
            top_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo))
        }

        // Top - crypto, bottom - fiat
        if (bottomCurrency.isFiat() && topCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) 1 / ticker.priceFiatAnalogue.toDouble()
                    else 1 / ticker.priceUsd.toDouble()
            top_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo))
        }

        // Both fiat
        if (topCurrency.isFiat() && bottomCurrency.isFiat()) {
            val topPriceFiat =
                    if (topTicker.priceFiatAnalogue.isNotEmpty()) topTicker.priceFiatAnalogue
                    else topTicker.priceUsd

            val bottomPriceFiat =
                    if (bottomTicker.priceFiatAnalogue.isNotEmpty()) bottomTicker.priceFiatAnalogue
                    else bottomTicker.priceUsd

            top_amount.setText(calculateConversion(
                    amount.toDouble(), topPriceFiat.toDouble() / bottomPriceFiat.toDouble()))
        }

        // Both crypto
        if (topCurrency.isCrypto() && bottomCurrency.isCrypto()) {
            val priceToConvertTo = if (ticker.priceFiatAnalogue == "")
                1 / ticker.priceBtc.toDouble()
            else
                1 / ticker.priceFiatAnalogue.toDouble()

            top_amount.setText(calculateConversion(
                    amount.toDouble(), priceToConvertTo))
        }
    }
    // endregion Conversion

    // region View
    override fun getLayout() = R.layout.activity_converter

    override fun proceedCryptToAnyConversion(ticker: TickerResponse) {
        this.ticker = ticker
        performTopToBottomConversion()
    }

    override fun proceedFiatToFiatConversion(tickers: List<TickerResponse>) {
        if (tickers.size != 2) {
            toast(R.string.error)
            return
        }

        topTicker = tickers[0]
        bottomTicker = tickers[1]
        performTopToBottomConversion()
    }

    override fun startToolbarLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun stopToolbarLoading() {
        swipe_refresh.isRefreshing = false
    }

    override fun disableAmountFields() {
        top_amount.isEnabled = false
        top_amount.isFocusable = false
        bottom_amount.isEnabled = false
        bottom_amount.isFocusable = false
    }

    override fun enableAmountFields() {
        top_amount.isEnabled = true
        bottom_amount.isEnabled = true
        top_amount.isFocusableInTouchMode = true
        bottom_amount.isFocusableInTouchMode = true
    }
    // endregion View

    companion object {
        val PICK_TOP_CONVERT_CURRENCY = 1
        val PICK_BOTTOM_CONVERT_CURRENCY = 2
    }
}