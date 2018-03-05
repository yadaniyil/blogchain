package com.yadaniil.blogchain.screens.findcurrency.favourite

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository

/**
 * Created by danielyakovlev on 11/15/17.
 */

class FindFavouriteViewModel(private val repo: Repository) : ViewModel() {

    fun getFavouriteCoinsLiveData() = repo.getAllFavouriteCoinsLiveData()

    fun getFavouriteCoinsFiltered(text: String) = repo.getFavouriteCoinsFiltered(text)
}