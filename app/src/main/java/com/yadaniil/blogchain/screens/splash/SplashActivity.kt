package com.yadaniil.blogchain.screens.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.yadaniil.blogchain.screens.home.HomeActivity
import io.fabric.sdk.android.Fabric


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}