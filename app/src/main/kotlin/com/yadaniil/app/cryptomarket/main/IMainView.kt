package com.yadaniil.app.cryptomarket.main

import com.arellomobile.mvp.MvpView

interface IMainView : MvpView {
    fun showLoading()
    fun stopLoading()
    fun updateList()
}