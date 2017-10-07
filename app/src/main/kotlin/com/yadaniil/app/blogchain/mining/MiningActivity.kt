package com.yadaniil.app.blogchain.mining

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.yadaniil.app.blogchain.R
import com.yadaniil.app.blogchain.mining.fragments.calculator.CalculatorFragment
import com.yadaniil.app.blogchain.mining.fragments.coins.CoinsFragment
import com.yadaniil.app.blogchain.mining.fragments.miners.MinersFragment
import kotlinx.android.synthetic.main.activity_mining.*

/**
 * Created by danielyakovlev on 9/20/17.
 */

class MiningActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mining)
        initBottomBar()
    }

    private fun initBottomBar() {
        bottomBar.setDefaultTabPosition(TAB_CALCULATOR)
        bottomBar.setOnTabSelectListener { tabId ->
            when(tabId) {
                R.id.tab_miners -> openFragment(MinersFragment.newInstance())
                R.id.tab_calculator -> openFragment(CalculatorFragment.newInstance())
                R.id.tab_coins -> openFragment(CoinsFragment.newInstance())
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
//        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
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