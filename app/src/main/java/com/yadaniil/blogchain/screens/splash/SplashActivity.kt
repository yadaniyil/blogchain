package com.yadaniil.blogchain.screens.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.yadaniil.blogchain.screens.base.BaseHelper
import com.yadaniil.blogchain.utils.Navigator
import io.fabric.sdk.android.Fabric
import org.koin.android.architecture.ext.viewModel


class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        // To select tab in nav drawer
        BaseHelper.selectedDrawerItem = BaseHelper.DRAWER_ITEM_ALL_COINS_ID
        viewModel.loadAllCoins { Navigator.toAllCoinsActivity(this) }
    }
}