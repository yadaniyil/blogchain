package com.yadaniil.blogchain.screens.mining.fragments.coins

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoin
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.CryptocurrencyHelper
import com.yadaniil.blogchain.utils.Endpoints
import com.yadaniil.blogchain.utils.ImageLoader
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import java.math.BigDecimal
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/28/17.
 */

class CoinsAdapter(context: Context, coinClickListener: CoinItemClickListener,
                   private val currencies: List<CoinMarketCapCurrencyRealm>,
                   private val ccCurrencies: List<CryptoCompareCurrencyRealm>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var coins: MutableList<MiningCoin>? = ArrayList()
    private var context: Context by Delegates.notNull()
    private var clickListener: CoinItemClickListener by Delegates.notNull()

    init {
        this.context = context
        this.clickListener = coinClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currentHolder = holder as CoinViewHolder
        val coin = coins!![position]
        val cmcCurrency = currencies.find { it.symbol == coin.tag }
        val btcCmcCurrency = currencies.find { it.symbol == "BTC" }

        currentHolder.name.text = "${coin.name}(${coin.tag})"
        currentHolder.algorithm.text = coin.algorithm
        currentHolder.difficulty.text = if(coin.difficulty == "1") "?" else coin.difficulty
        currentHolder.revenue24.text = getPriceInUsd(coin, btcCmcCurrency)
        currentHolder.profitability.text = coin.profitability24.toString() + "%"
        currentHolder.equipmentType.text = coin.equipmentType

        val iconLink = CryptocurrencyHelper.getImageLinkForCurrency(cmcCurrency, ccCurrencies)
        if (iconLink.isNotEmpty())
            ImageLoader.load(Endpoints.CRYPTO_COMPARE_URL + iconLink, currentHolder.icon, context)
        else
            currentHolder.icon.setImageResource(R.drawable.icon_ico)

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, coin)
        }
    }

    private fun getPriceInUsd(coin: MiningCoin, btc: CoinMarketCapCurrencyRealm?): String {
        return "$${AmountFormatter.formatFiatPrice(BigDecimal(coin.btcRevenue24)
                .multiply(BigDecimal(btc?.priceUsd ?: 0.0)))}"
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_mining_coin_list, parent, false)
        return CoinViewHolder(v)
    }

    fun setData(coins: List<MiningCoin>) {
        this.coins?.clear()
        this.coins?.addAll(coins)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return coins?.size!!
    }

    class CoinViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val itemLayout: LinearLayout = v.find(R.id.item_coin_layout)
        val icon: ImageView = v.find(R.id.item_coin_icon)
        val name: TextView = v.find(R.id.item_coin_name)
        val algorithm: TextView = v.find(R.id.item_coin_algorithm)
        val difficulty: TextView = v.find(R.id.item_coin_difficulty)
        val revenue24: TextView = v.find(R.id.item_coin_revenue24)
        val profitability: TextView = v.find(R.id.item_coin_profitability)
        val equipmentType: TextView = v.find(R.id.item_coin_equipment_type)
    }
}
