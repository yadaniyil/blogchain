package com.yadaniil.blogchain.screens.converter

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.CurrencyHelper
import com.yadaniil.blogchain.utils.FiatCurrenciesHelper
import com.yadaniil.blogchain.utils.ListHelper
import com.yadaniil.blogchain.utils.Navigator
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_converter.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 11/15/17.
 */

class ConverterActivity : BaseActivity(), ConverterView {

    @InjectPresenter
    lateinit var presenter: ConverterPresenter

    private lateinit var allCoins: RealmResults<CoinMarketCapCurrencyRealm>
    private lateinit var allCcCoins: RealmResults<CryptoCompareCurrencyRealm>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allCoins = presenter.getAllCoins()
        allCcCoins = presenter.getAllCcCoins()
        initAdMobBanner()
        initTopCurrency()
        initBottomCurrency()
    }

    private fun initTopCurrency() {
        val topCurrencySymbol = top_currency.find<TextView>(R.id.currency_symbol)
        val topCurrencyName = top_currency.find<TextView>(R.id.currency_name)
        val topCurrencyIcon = top_currency.find<ImageView>(R.id.currency_icon)

        topCurrencySymbol.text = allCoins[0].symbol
        topCurrencyName.text = allCoins[0].name
        CurrencyHelper.getImageLinkForCurrency(allCoins[0], allCcCoins)
        ListHelper.downloadAndSetIcon(topCurrencyIcon, allCoins[0], allCcCoins, this)
        top_currency.onClick { Navigator.toFindCurrencyActivity(this, PICK_CONVERT_CURRENCY) }
    }

    private fun initBottomCurrency() {
        val bottomCurrencySymbol = bottom_currency.find<TextView>(R.id.currency_symbol)
        val bottomCurrencyName = bottom_currency.find<TextView>(R.id.currency_name)
        val bottomCurrencyIcon = bottom_currency.find<ImageView>(R.id.currency_icon)

        val fiatCurrencies = FiatCurrenciesHelper.getAll(this)
        bottomCurrencySymbol.text = fiatCurrencies[0].symbol
        bottomCurrencyName.text = fiatCurrencies[0].name
        bottomCurrencyIcon.setImageDrawable(resources.getDrawable(fiatCurrencies[0].iconIntRes))
        bottom_currency.onClick { Navigator.toFindCurrencyActivity(this, PICK_CONVERT_CURRENCY) }
    }

    private fun initAdMobBanner() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        adView.loadAd(builder)
    }

    override fun getLayout() = R.layout.activity_converter


    companion object {
        val PICK_CONVERT_CURRENCY = 1
    }
}