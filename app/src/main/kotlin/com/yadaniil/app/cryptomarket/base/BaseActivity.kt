package com.yadaniil.app.cryptomarket.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.yadaniil.app.cryptomarket.R
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.yadaniil.app.cryptomarket.mining.MiningActivity
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


abstract class BaseActivity : MvpAppCompatActivity(), IBaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        setSupportActionBar(toolbar)
        setUpNavigationDrawer()
    }

    private fun setUpNavigationDrawer() {
        val item1 = PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.drawer_item_currencies).withIcon(R.drawable.icon_list_numbered)
        val item2 = PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.drawer_item_portfolio).withIcon(R.drawable.icon_portfolio).withSelectable(false)
        val item3 = PrimaryDrawerItem().withIdentifier(3)
                .withName(R.string.drawer_item_exchanges).withIcon(R.drawable.icon_exchanges).withSelectable(false)
        val item4 = PrimaryDrawerItem().withIdentifier(4)
                .withName(R.string.drawer_item_ico).withIcon(R.drawable.icon_ico).withSelectable(false)
        val item5 = PrimaryDrawerItem().withIdentifier(5)
                .withName(R.string.drawer_item_mining).withIcon(R.drawable.icon_mining).withSelectable(false)
                .withOnDrawerItemClickListener { _, _, _-> startActivity<MiningActivity>(); false }
        val item6 = PrimaryDrawerItem().withIdentifier(6)
                .withName(R.string.drawer_item_settings).withIcon(R.drawable.icon_settings).withSelectable(false)

        val headerResult = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_bar_header_background)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(ProfileDrawerItem()
                                .withName(R.string.add_google_account)
                                .withIcon(resources.getDrawable(R.drawable.ic_account_circle_black_24dp)))
                .withOnAccountHeaderListener({ view, profile, currentProfile ->
                    toast("To add google account activity")
                    true
                })
                .build()

        val drawer = DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withSelectedItem(-1)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(item1, item2, item3, item4, item5, DividerDrawerItem(), item6)
                .build()
        drawer.header.onClick { toast("To add google account activity") }
    }

    abstract fun getLayout(): Int
}

