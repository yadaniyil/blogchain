package com.yadaniil.blogchain.screens.news

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import com.yadaniil.blogchain.R
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_webview.*
import android.webkit.WebViewClient


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

        progressBar.max = 100
        progressBar.progressDrawable = resources.getDrawable(R.drawable.horizontal_progress)
        progressBar.progressDrawable.setColorFilter(resources.getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY)

        webview.webViewClient = WebViewClientDemo()
        webview.webChromeClient = WebChromeClientDemo()

        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(true)
        webview.settings.builtInZoomControls = true
        webview.loadUrl(url)
    }

    inner class WebViewClientDemo : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.toString())
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
            progressBar.progress = 100
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
            progressBar.progress = 0
        }
    }

    inner class WebChromeClientDemo : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.progress = newProgress
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}