package com.yadaniil.blogchain.screens.news

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.utils.DateHelper
import com.yadaniil.blogchain.utils.ImageLoader
import com.yadaniil.blogchain.utils.ListHelper
import org.jetbrains.anko.onClick
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator


/**
 * Created by danielyakovlev on 12/23/17.
 */


class NewsAdapter(private val context: Context,
                  private val onNewsClick: OnNewsClick) : RecyclerView.Adapter<ListHelper.NewsHolder>() {

    private val items: MutableList<NewsModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.NewsHolder {
        val view =  LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
        return ListHelper.NewsHolder(view)
    }

    override fun onBindViewHolder(holder: ListHelper.NewsHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.sourceName.text = item.sourceName
        holder.pubDate.text = DateHelper.getTimeAgo(item.publishDate, context)
        holder.rootView.onClick { onNewsClick.onClick(holder, item) }
        if(item.imageLink.isEmpty())
            setDrawableTitleImage(item, holder)
        else
            ImageLoader.load(item.imageLink, holder.image, context)
    }

    private fun setDrawableTitleImage(item: NewsModel, holder: ListHelper.NewsHolder?) {
        val generator = ColorGenerator.MATERIAL
        val randomColor = generator.getColor(item.sourceName)

        val drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.BLACK)
                .useFont(Typeface.DEFAULT)
                .fontSize(40)
                .bold()
                .endConfig()
                .buildRoundRect(item.sourceName, randomColor, 10)

        holder?.image?.setImageDrawable(drawable)
    }

    override fun getItemCount() = items.size

    fun updateNews(news: List<NewsModel>) {
        this.items.clear()
        this.items.addAll(news)
    }
}