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
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity
import com.yadaniil.blogchain.screens.findcurrency.fiat.FiatCurrencyItem
import com.yadaniil.blogchain.utils.CryptocurrencyHelper
import com.yadaniil.blogchain.utils.FiatCurrenciesHelper
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_converter.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


/**
 * Created by danielyakovlev on 11/15/17.
 */

class ConverterActivity : BaseActivity(), ConverterView {

    @InjectPresenter
    lateinit var presenter: ConverterPresenter

    private lateinit var allCoins: RealmResults<CoinMarketCapCurrencyRealm>
    private lateinit var allCcCoins: RealmResults<CryptoCompareCurrencyRealm>

    private lateinit var topCurrency: ConverterCurrency
    private lateinit var bottomCurrency: ConverterCurrency
    private lateinit var allFiatCurrencies: List<FiatCurrencyItem>

    private lateinit var topAmountSubscription: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        allCoins = presenter.getAllCoins()
        allCcCoins = presenter.getAllCcCoins()
        allFiatCurrencies = FiatCurrenciesHelper.getAll(this)
        initAdMobBanner()
        initTopCurrency()
        initBottomCurrency()

        topAmountSubscription = RxTextView.afterTextChangeEvents(top_amount).skipInitialValue()
                .map { it.editable().toString() }
                .observeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .switchMap { presenter.downloadTickerWithConversion(
                        (topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol) }
                .subscribe { toast("done") }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!topAmountSubscription.isDisposed) {
            topAmountSubscription.dispose()
        }
    }

    private fun updateConversionResults() {
        presenter.showBothCryptoConversion((topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_converter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_swap) {
            toast("SWAP BITCH!")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initTopCurrency(coin: CoinMarketCapCurrencyRealm? = null) {
        val topCurrencySymbol = top_currency.find<TextView>(R.id.currency_symbol)
        val topCurrencyName = top_currency.find<TextView>(R.id.currency_name)
        val topCurrencyIcon = top_currency.find<ImageView>(R.id.currency_icon)

        if(coin == null) {
            topCurrencySymbol.text = allCoins[0].symbol
            topCurrencyName.text = allCoins[0].name
            ListHelper.downloadAndSetIcon(topCurrencyIcon, allCoins[0], allCcCoins, this)
            topCurrency = ConverterCryptoCurrency(allCoins[0].symbol ?: "", allCoins[0]?.id ?: "")
        } else {
            topCurrencySymbol.text = coin.symbol
            topCurrencyName.text = coin.name
            ListHelper.downloadAndSetIcon(topCurrencyIcon, coin, allCcCoins, this)
            topCurrency = ConverterCryptoCurrency(coin.symbol ?: "", coin.id ?: "")
        }

        top_currency.onClick { Navigator.toFindCurrencyActivity(this, PICK_TOP_CONVERT_CURRENCY) }
    }

    private fun initBottomCurrency(coin: CoinMarketCapCurrencyRealm? = null) {
        val bottomCurrencySymbol = bottom_currency.find<TextView>(R.id.currency_symbol)
        val bottomCurrencyName = bottom_currency.find<TextView>(R.id.currency_name)
        val bottomCurrencyIcon = bottom_currency.find<ImageView>(R.id.currency_icon)

        if(coin == null) {
            bottomCurrencySymbol.text = allFiatCurrencies[0].symbol
            bottomCurrencyName.text = allFiatCurrencies[0].name
            bottomCurrencyIcon.setImageDrawable(resources.getDrawable(allFiatCurrencies[0].iconIntRes))
            bottomCurrency = ConverterFiatCurrency(allFiatCurrencies[0].symbol)
        } else {
            bottomCurrencySymbol.text = coin.symbol
            bottomCurrencyName.text = coin.name
            ListHelper.downloadAndSetIcon(bottomCurrencyIcon, coin, allCcCoins, this)
            bottomCurrency = ConverterFiatCurrency(coin.symbol ?: "")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_TOP_CONVERT_CURRENCY) {
            if (resultCode == RESULT_OK) {
//                val fiatCurrency
                val coin = presenter.getCoin(data?.extras?.getString(FindCurrencyActivity.PICKED_COIN_SYMBOL) ?: "")

                initTopCurrency(coin)
            }
        } else if(requestCode == PICK_BOTTOM_CONVERT_CURRENCY) {
            if (resultCode == RESULT_OK) {
                val coin = presenter.getCoin(data?.extras?.getString(FindCurrencyActivity.PICKED_COIN_SYMBOL) ?: "")
                initBottomCurrency(coin)
            }
        }

        initConversion()
    }

    private fun initConversion() {
        // Determine is both crypto, fiat or different currency type

        // Top - fiat, bottom - crypto
        if(FiatCurrenciesHelper.isFiat(topCurrency.symbol, allFiatCurrencies) &&
                CryptocurrencyHelper.isCrypto(bottomCurrency.symbol, allCoins)) {

        }

        // Top - crypto, bottom - fiat
        if(FiatCurrenciesHelper.isFiat(bottomCurrency.symbol, allFiatCurrencies) &&
                CryptocurrencyHelper.isCrypto(topCurrency.symbol, allCoins)) {

        }

        // Both fiat
        if(FiatCurrenciesHelper.isFiat(topCurrency.symbol, allFiatCurrencies) &&
                FiatCurrenciesHelper.isFiat(bottomCurrency.symbol, allFiatCurrencies)) {

        }

        // Both crypto
        if(CryptocurrencyHelper.isCrypto(topCurrency.symbol, allCoins) &&
                CryptocurrencyHelper.isCrypto(bottomCurrency.symbol, allCoins)) {
//            presenter.showBothCryptoConversion(
//                    (topCurrency as ConverterCryptoCurrency).id, bottomCurrency.symbol)
        }


    }

    override fun getLayout() = R.layout.activity_converter

    override fun setConversionValues(topCurrency: String, bottomCurrency: String) {

        top_amount.setText(topCurrency)
        bottom_amount.setText(bottomCurrency)
    }

    override fun startToolbarLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun stopToolbarLoading() {
        swipe_refresh.isRefreshing = false
    }

    companion object {
        val PICK_TOP_CONVERT_CURRENCY = 1
        val PICK_BOTTOM_CONVERT_CURRENCY = 2
    }
}