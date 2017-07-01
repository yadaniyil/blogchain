package com.yadaniil.app.cryptomarket.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(), IBaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

