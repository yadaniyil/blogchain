package com.yadaniil.app.cryptomarket.splash

import com.yadaniil.app.cryptomarket.main.MainActivity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.os.Bundle
import com.yadaniil.app.cryptomarket.base.BaseActivity


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}