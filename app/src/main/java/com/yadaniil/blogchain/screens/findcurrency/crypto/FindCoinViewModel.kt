package com.yadaniil.blogchain.screens.findcurrency.crypto

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository

/**
 * Created by danielyakovlev on 11/15/17.
 */

class FindCoinViewModel(private val repo: Repository) : ViewModel() {

    fun getAllCoins() = repo.getAllCoinsFromDb()

    fun getAllCoinsFiltered(text: String) = repo.getAllCoinsFiltered(text)
}
