package com.yadaniil.blogchain.screens.mining

import android.os.Bundle
import android.support.v4.app.Fragment
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.screens.mining.fragments.calculator.CalculatorFragment
import com.yadaniil.blogchain.screens.mining.fragments.coins.CoinsFragment
import com.yadaniil.blogchain.screens.mining.fragments.miners.MinersFragment
import kotlinx.android.synthetic.main.activity_mining.*

/**
 * Created by danielyakovlev on 9/20/17.
 */

class MiningActivity : BaseActivity() {

    override fun getLayout() = R.layout.activity_mining

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomBar()
    }

    private fun initBottomBar() {
        val openAndCloseDrawerAction = {
            if(drawer.isDrawerOpen)
                drawer.closeDrawer()
            else drawer.openDrawer()
        }
        bottomBar.setDefaultTabPosition(TAB_CALCULATOR)
        bottomBar.setOnTabSelectListener { tabId ->
            when(tabId) {
                R.id.tab_miners -> openFragment(MinersFragment.newInstance(openAndCloseDrawerAction))
                R.id.tab_calculator -> openFragment(CalculatorFragment.newInstance(openAndCloseDrawerAction))
                R.id.tab_coins -> openFragment(CoinsFragment.newInstance(openAndCloseDrawerAction))
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        val fragmentByTag = supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
        if (fragmentByTag != null) {
            ft.replace(R.id.contentContainer, fragmentByTag, fragmentByTag.javaClass.simpleName)
        } else {
            ft.replace(R.id.contentContainer, fragment, fragment.javaClass.simpleName)
        }
        ft.commit()
    }


    companion object {
        val TAB_MINERS = 0
        val TAB_CALCULATOR = 1
        val TAB_COINS = 2
    }
}