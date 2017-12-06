package com.yadaniil.blogchain.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.data.Repository

/**
 * Created by danielyakovlev on 12/5/17.
 */

object ImageLoader {
    fun load(url: String, imageView: ImageView, context: Context) =
            Picasso.with(context).load(Uri.parse(url)).into(imageView)

    fun loadCoinIcon(symbol: String, imageView: ImageView, context: Context, repo: Repository,
                     loadIfNoSuchCoin: String? = null) {
        val ccCoins = repo.getAllCryptoCompareCoinsFromDb()
        val coin = repo.getCoinFromDb(symbol)
        val imageLink = CryptocurrencyHelper.getImageLinkForCurrency(coin, ccCoins)
        if (imageLink.isEmpty())
            load(loadIfNoSuchCoin ?: "", imageView, context)
        else
            load(Endpoints.CRYPTO_COMPARE_URL + imageLink, imageView, context)
    }
}