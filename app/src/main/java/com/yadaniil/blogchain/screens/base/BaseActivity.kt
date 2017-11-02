package com.yadaniil.blogchain.screens.base

import android.os.Bundle
import android.os.Handler
import com.arellomobile.mvp.MvpAppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.yadaniil.blogchain.R
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.yadaniil.blogchain.utils.Navigator
import org.jetbrains.anko.toast
import timber.log.Timber


abstract class BaseActivity : MvpAppCompatActivity(), BaseView {

    private lateinit var interstitialAd: InterstitialAd
    lateinit var drawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        setSupportActionBar(toolbar)
        setUpNavigationDrawer()
        initAdMobInterstitial()

    }

    private fun setUpNavigationDrawer() {
        val home = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_HOME_ID,
                R.string.dashboard, R.drawable.icon_home) {
            BaseHelper.selectedDrawerItem = BaseHelper.DRAWER_ITEM_HOME_ID
            Handler().postDelayed({ Navigator.toHomeActivity(this) }, 500)
        }

        val allCoins = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_ALL_COINS_ID,
                R.string.all_coins, R.drawable.ic_format_list_numbered_24dp) {
            BaseHelper.selectedDrawerItem = BaseHelper.DRAWER_ITEM_ALL_COINS_ID
            Handler().postDelayed({ Navigator.toAllCoinsActivity(this) }, 500)
        }

        val marketInfo = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_MARKET_INFO_ID,
                R.string.drawer_item_market_info, R.drawable.icon_market_info, enabled = false) {}

        val converter = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_CONVERTER_ID,
                R.string.drawer_item_converter, R.drawable.icon_converter, enabled = false) {}

        val watchlist = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_WATCHLIST_ID,
                R.string.drawer_item_watchlist, R.drawable.ic_eye_24dp) {
            BaseHelper.selectedDrawerItem = BaseHelper.DRAWER_ITEM_WATCHLIST_ID
            Handler().postDelayed({ Navigator.toWatchlistActivity(this) }, 500)
        }

        val portfolio = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_PORTFOLIO_ID,
                R.string.drawer_item_portfolio, R.drawable.icon_portfolio, enabled = false) {}

        val exchanges = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_EXCHANGES_ID,
                R.string.drawer_item_exchanges, R.drawable.icon_exchanges, enabled = false) {}

        val ico = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_ICO_ID,
                R.string.drawer_item_ico, R.drawable.icon_ico, enabled = false) {}

        val mining = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_MINING_ID,
                R.string.drawer_item_mining, R.drawable.icon_mining) {
            BaseHelper.selectedDrawerItem = BaseHelper.DRAWER_ITEM_MINING_ID
            Handler().postDelayed({ Navigator.toMiningActivity(this) }, 500)
        }

        val settings = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_SETTINGS_ID,
                R.string.drawer_item_settings, R.drawable.icon_settings, enabled = false) {}

        val ad = BaseHelper.primaryItem(BaseHelper.DRAWER_ITEM_AD_ID,
                R.string.drawer_item_ad, R.drawable.ic_warning_gray_24dp) {
            showInterstitialAd()
        }

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_bar_header_background)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(ProfileDrawerItem()
                        .withName(R.string.add_google_account)
                        .withIcon(resources.getDrawable(R.drawable.ic_account_circle_black_24dp)))
                .withOnAccountHeaderListener({ view, profile, currentProfile ->
                    //                    toast("To add google account activity")
                    true
                })
                .build()

        drawer = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withSelectedItem(BaseHelper.selectedDrawerItem)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(home, allCoins, watchlist, portfolio, converter, exchanges, ico, mining, marketInfo,
                        DividerDrawerItem(), settings, ad)
                .build()
//        drawer.header.onLongClick { toast("To add google account activity") }
    }

    private fun showInterstitialAd() {
        if (interstitialAd.isLoaded)
            interstitialAd.show()
        else {
            Timber.e("The interstitial wasn't loaded yet.")
            toast(R.string.not_so_fast)
        }

    }

    abstract fun getLayout(): Int

    private fun initAdMobInterstitial() {
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.dont_touch_interstitial)
        val builder = AdRequest.Builder()
                .addTestDevice(getString(R.string.admob_test_device))
                .build()
        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                interstitialAd.loadAd(builder)
            }
        }
        interstitialAd.loadAd(builder)
    }
}

