package com.yadaniil.blogchain.screens.portfolio

import android.os.Bundle
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.Navigator
import kotlinx.android.synthetic.main.activity_portfolio.*
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 11/3/17.
 */

class PortfolioActivity : BaseActivity(), PortfolioView {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        total_amount.text = "14,566.95 USD"
        fab.onClick { Navigator.toAddCoinToPortfolioActivity(this) }

    }

    override fun getLayout() = R.layout.activity_portfolio

}