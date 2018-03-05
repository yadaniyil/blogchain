package com.yadaniil.blogchain.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity


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
//        viewModel.saveCurrencyIcon(currencyRealm, byteArray)
    }


    fun loadCoinIcon(coinEntity: CoinEntity?, imageView: ImageView, context: Context,
                     loadIfNoSuchCoin: String? = null, drawableIntRes: Int? = null) {
        val imageLink = coinEntity?.ccImageUrl ?: ""
        if (imageLink.isEmpty() && loadIfNoSuchCoin != null)
            load(loadIfNoSuchCoin, imageView, context)
        else if (imageLink.isEmpty() && drawableIntRes != null)
            imageView.setImageResource(drawableIntRes)
        else if (imageLink.isEmpty() && loadIfNoSuchCoin == null)
            imageView.setImageResource(R.drawable.icon_ico)
        else {
            load(Endpoints.CRYPTO_COMPARE_URL + imageLink, imageView, context)
        }
    }
}