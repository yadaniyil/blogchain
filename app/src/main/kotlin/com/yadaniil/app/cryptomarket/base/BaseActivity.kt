package com.yadaniil.app.cryptomarket.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.DrawerBuilder
import com.yadaniil.app.cryptomarket.R
import org.jetbrains.anko.find


abstract class BaseActivity : AppCompatActivity(), IBaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems()
                .build()
    }

    abstract fun getLayout(): Int
}

