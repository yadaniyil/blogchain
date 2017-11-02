package com.yadaniil.blogchain.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.yadaniil.blogchain.R
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.yadaniil.blogchain.mining.MiningActivity
import com.yadaniil.blogchain.watchlist.WatchlistActivity
import org.jetbrains.anko.startActivity
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
        val item1 = PrimaryDrawerItem().withIdentifier(1).withEnabled(false)
                .withName(R.string.drawer_item_market_info).withIcon(R.drawable.icon_market_info).withSelectable(false)

        val item2 = PrimaryDrawerItem().withIdentifier(2).withEnabled(false)
                .withName(R.string.drawer_item_converter).withIcon(R.drawable.icon_converter).withSelectable(false)

        val item3 = PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_watchlist)
                .withIcon(R.drawable.ic_eye_24dp).withSelectable(false)
                .withOnDrawerItemClickListener { _, _, _-> startActivity<WatchlistActivity>(); false }

        val item4 = PrimaryDrawerItem().withIdentifier(4).withEnabled(false)
                .withName(R.string.drawer_item_portfolio).withIcon(R.drawable.icon_portfolio).withSelectable(false)

        val item5 = PrimaryDrawerItem().withIdentifier(5).withEnabled(false)
                .withName(R.string.drawer_item_exchanges).withIcon(R.drawable.icon_exchanges).withSelectable(false)

        val item6 = PrimaryDrawerItem().withIdentifier(6).withEnabled(false)
                .withName(R.string.drawer_item_ico).withIcon(R.drawable.icon_ico).withSelectable(false)

        val item7 = PrimaryDrawerItem().withIdentifier(7)
                .withName(R.string.drawer_item_mining).withIcon(R.drawable.icon_mining).withSelectable(false)
                .withOnDrawerItemClickListener { _, _, _-> startActivity<MiningActivity>(); false }

        val item8 = PrimaryDrawerItem().withIdentifier(8).withEnabled(false)
                .withName(R.string.drawer_item_settings).withIcon(R.drawable.icon_settings).withSelectable(false)

        val item9 = PrimaryDrawerItem().withIdentifier(9).withSelectable(false)
                .withName(R.string.drawer_item_ad).withIcon(R.drawable.ic_warning_gray_24dp)
                .withOnDrawerItemClickListener { _, _, _-> showInterstitialAd(); false }

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
                .withSelectedItem(-1)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(item1, item2, item3, item4, item5, item6, item7,
                        DividerDrawerItem(), item8, item9)
                .build()
//        drawer.header.onClick { toast("To add google account activity") }
    }

    private fun showInterstitialAd() {
        if(interstitialAd.isLoaded)
            interstitialAd.show()
        else
            Timber.e("The interstitial wasn't loaded yet.")
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

