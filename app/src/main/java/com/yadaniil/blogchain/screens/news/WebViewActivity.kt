package com.yadaniil.blogchain.screens.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.yadaniil.blogchain.R
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_webview.*
import org.jetbrains.anko.find


/**
 * Created by danielyakovlev on 12/24/17.
 */

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val url = intent.extras.getString("url")
        val source = intent.extras.getString("source")

        toolbar.title = source
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val webView = find<WebView>(R.id.webview)
        webView.loadUrl(url)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}