package com.yadaniil.blogchain.screens.converter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.TickerResponse
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity
import com.yadaniil.blogchain.screens.findcurrency.fiat.FiatCurrencyItem
import com.yadaniil.blogchain.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_converter.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import org.jetbrains.anko.onClick
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

    // region Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allCoins = presenter.getAllCoins()
        allCcCoins = presenter.getAllCcCoins()
        allFiatCurrencies = FiatCurrenciesHelper.getAll(this)
        initAdMobBanner()
        initTopCurrency()
        initBottomCurrency()
        updateTicker()
        initSwipeToRefresh()
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
        }
        return super.onOptionsItemSelected(item)
    }
    // endregion Activity

    // region Init
    private fun initConversionAmounts() {
        topAmountSubscription = RxTextView.afterTextChangeEvents(top_amount).skipInitialValue()
                .map { it.editable().toString() }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { initTopToBottomConversion() }

        bottomAmountSubscription = RxTextView.afterTextChangeEvents(bottom_amount).skipInitialValue()
                .map { it.editable().toString() }
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { initBottomToTopConversion() }
    }

    private fun initSwipeToRefresh() {
        RxSwipeRefreshLayout.refreshes(swipe_refresh)
                .doOnSubscribe { disableAmountFields() }
                .doOnComplete { enableAmountFields() }
                .switchMap {
                    presenter.downloadTickerWithConversion(
                            (topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol)
                }
                .subscribe {
                    swipe_refresh.isRefreshing = false
                    initTopToBottomConversion()
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
            ListHelper.downloadAndSetIcon(topCurrencyIcon, allCoins[0], allCcCoins, this)
            topCurrency = ConverterCryptoCurrency(allCoins[0].symbol ?: "", allCoins[0]?.id ?: "")
        } else if (coin != null && fiat == null) {
            // Crypto
            topCurrencySymbol.text = coin.symbol
            topCurrencyName.text = coin.name
            ListHelper.downloadAndSetIcon(topCurrencyIcon, coin, allCcCoins, this)
            topCurrency = ConverterCryptoCurrency(coin.symbol ?: "", coin.id ?: "")
        } else if (coin == null && fiat != null) {
            // Fiat
            topCurrencySymbol.text = fiat.symbol
            topCurrencyName.text = fiat.name
            topCurrencyIcon.setImageDrawable(resources.getDrawable(fiat.iconIntRes))
            bottomCurrency = ConverterFiatCurrency(fiat.symbol)
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
            ListHelper.downloadAndSetIcon(bottomCurrencyIcon, coin, allCcCoins, this)
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

    private fun initAdMobBanner() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        adView.loadAd(builder)
    }

    private fun initTopToBottomConversion() {
        if (top_amount.text.isEmpty()) {
            bottom_amount.text.clear()
            return
        }

        // Top - fiat, bottom - crypto
        if (topCurrency.isFiat() && bottomCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) ticker.priceFiatAnalogue
                    else ticker.priceUsd
            bottom_amount.setText(
                    calculateConversion(top_amount.text.toString(), priceToConvertTo))
        }

        // Top - crypto, bottom - fiat
        if (bottomCurrency.isFiat() && topCurrency.isCrypto()) {
            val priceToConvertTo =
                    if (ticker.priceFiatAnalogue.isNotEmpty()) ticker.priceFiatAnalogue
                    else ticker.priceUsd
            bottom_amount.setText(
                    calculateConversion(top_amount.text.toString(), priceToConvertTo))
        }

        // Both fiat
        if (topCurrency.isFiat() && bottomCurrency.isFiat()) {

        }

        Timber.e("ticker.priceBtc: " + ticker.priceBtc)
        Timber.e("ticker.priceFiatAnalogue: " + ticker.priceFiatAnalogue)
//        Timber.e("priceToConvertTo: " + priceToConvertTo)

        // Both crypto
        if (topCurrency.isCrypto() && bottomCurrency.isCrypto()) {
            var priceToConvertTo: String

            if (ticker.priceFiatAnalogue == "") {
                priceToConvertTo = ticker.priceBtc
            } else {
                priceToConvertTo = ticker.priceFiatAnalogue
            }
            bottom_amount.setText(calculateConversion(top_amount.text.toString(), priceToConvertTo))
        }
    }

    private fun initBottomToTopConversion() {

    }
    // endregion Init

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

    private fun updateTicker() = presenter.downloadTicker(
            (topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol)

    private fun calculateConversion(amount: String, rate: String): String {
        return AmountFormatter.formatCryptoPrice(BigDecimal(amount.toDouble() * rate.toDouble()))
    }


    private fun ConverterCurrency.isCrypto() = CryptocurrencyHelper.isCrypto(this.symbol, allCoins)
    private fun ConverterCurrency.isFiat() = FiatCurrenciesHelper.isFiat(this.symbol, allFiatCurrencies)

    // region View
    override fun getLayout() = R.layout.activity_converter

    override fun setConversionValues(ticker: TickerResponse) {
        this.ticker = ticker
        initTopToBottomConversion()
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