package com.yadaniil.blogchain.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.Repository


/**
 * Created by danielyakovlev on 12/5/17.
 */

object ImageLoader {
    fun load(url: String, imageView: ImageView, context: Context) {

        Picasso.with(context).load(Uri.parse(url)).into(imageView)

        // Maybe in future I will save icons for reuse
//        val bitmap = (icon.drawable as BitmapDrawable).bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        val byteArray = stream.toByteArray()
//        presenter.saveCurrencyIcon(currencyRealm, byteArray)
    }


    fun loadCoinIcon(symbol: String, imageView: ImageView, context: Context,
                     repo: Repository, loadIfNoSuchCoin: String? = null) {
        val ccCoins = repo.getAllCryptoCompareCoinsFromDb()
        val coin = repo.getCoinFromDb(symbol)
        val imageLink = CryptocurrencyHelper.getImageLinkForCurrency(coin, ccCoins)
        if (imageLink.isEmpty() && loadIfNoSuchCoin != null)
            load(loadIfNoSuchCoin, imageView, context)
        else if(imageLink.isEmpty() && loadIfNoSuchCoin == null)
            imageView.setImageResource(R.drawable.icon_ico)
        else {
            load(Endpoints.CRYPTO_COMPARE_URL + imageLink, imageView, context)
        }
    }
}