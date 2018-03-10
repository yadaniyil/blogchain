package com.yadaniil.blogchain.screens.splash

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.screens.allcoins.AllCoinsHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by danielyakovlev on 3/9/18.
 */

class SplashViewModel(private val repo: Repository) : ViewModel() {

    fun loadAllCoins(onCoinsLoaded: () -> Unit) {
        doAsync {
            AllCoinsHelper.coins = repo.getAllCoinsFromDb()
            uiThread {
                onCoinsLoaded()
            }
        }

    }

}