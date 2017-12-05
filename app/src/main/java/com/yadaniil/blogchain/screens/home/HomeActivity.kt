package com.yadaniil.blogchain.screens.home

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.base.BaseActivity

/**
 * Created by danielyakovlev on 11/2/17.
 */

class HomeActivity : BaseActivity(), HomeView {

    @InjectPresenter
    lateinit var presenter: HomePresenter

    override fun getLayout() = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.showChangelogDialog()
        presenter.downloadAndSaveAllCurrencies()
    }


    override fun showChangelogDialog() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val prev = fm.findFragmentByTag("changelogdialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ChangelogDialog().show(ft, "changelogdialog")
    }
}