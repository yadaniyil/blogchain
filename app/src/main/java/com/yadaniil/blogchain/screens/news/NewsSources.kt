package com.yadaniil.blogchain.screens.news

import kotlin.collections.ArrayList

/**
 * Created by danielyakovlev on 12/6/17.
 */

object NewsSources {

    enum class SourceLanguage { ENG, RUS }

    class NewsSource(var feedRssLink: String, var sourceName: String, language: SourceLanguage)

    fun getAllSources(): List<NewsSource> {
        val allNewsSources: MutableList<NewsSource> = ArrayList()
        allNewsSources.addAll(getAllSourcesByLanguage(SourceLanguage.RUS))
        allNewsSources.addAll(getAllSourcesByLanguage(SourceLanguage.ENG))
        return allNewsSources
    }

    fun getAllSourcesByLanguage(language: SourceLanguage): List<NewsSource> {
        when (language) {
            SourceLanguage.ENG -> return listOf(
                    NewsSource("http://feeds.feedburner.com/CoinDesk", "CoinDesk", SourceLanguage.ENG),
                    NewsSource("https://cointelegraph.com/rss", "Cointelegraph", SourceLanguage.ENG),
                    NewsSource("https://bitcoinmagazine.com/feed/", "Bitcoin Magazine", SourceLanguage.ENG),
                    NewsSource("http://www.newsbtc.com/feed/", "NEWSBTC", SourceLanguage.ENG),
                    NewsSource("https://news.bitcoin.com/feed/", "bitcoin.com", SourceLanguage.ENG),
                    NewsSource("http://themerkle.com/feed/", "The Merkle", SourceLanguage.ENG),
                    NewsSource("http://news.8btc.com/feed/", "news.8btc.com", SourceLanguage.ENG),
                    NewsSource("https://feeds.feedburner.com/coinspeaker", "Coinspeaker", SourceLanguage.ENG),
                    NewsSource("http://bitsonline.com/feed/", "Bitsonline", SourceLanguage.ENG),
                    NewsSource("http://forklog.net/feed/", "Forklog", SourceLanguage.ENG),
                    NewsSource("https://www.cryptocoinsnews.com/feed/", "Cryptocoinsnews", SourceLanguage.ENG),
                    NewsSource("http://bitcoinist.net/feed/", "Bitcoinist", SourceLanguage.ENG),
                    NewsSource("https://www.blockchaintechnology-news.com/feed/", "The Block", SourceLanguage.ENG)
            )

            SourceLanguage.RUS -> return listOf(
                    NewsSource("https://bitnovosti.com/feed/", "BitNovosti", SourceLanguage.RUS),
                    NewsSource("https://forklog.com/feed/", "Forklog", SourceLanguage.RUS),
                    NewsSource("https://bitjournal.media/feed/", "bitjournal.media", SourceLanguage.RUS),
                    NewsSource("https://www.allcryptonews.com/feed/", "AllCryptoNews", SourceLanguage.RUS),
                    NewsSource("https://forum.bits.media/index.php?/rss/2-forum_news.xml/", "bits.media", SourceLanguage.RUS),
                    NewsSource("https://cryptocurrency.tech/feed/", "cryptocurrency.tech", SourceLanguage.RUS),
                    NewsSource("https://coinspot.io/feed/", "Coinspot", SourceLanguage.RUS),
                    NewsSource("http://crypto.by/feed/", "crypto.by", SourceLanguage.RUS),
                    NewsSource("https://ru.investing.com/rss/news_301.rss", "investing.com", SourceLanguage.RUS),
                    NewsSource("http://www.coinfox.info/rss-feed-en/main-feed-ru", "Coinfox", SourceLanguage.RUS),
                    NewsSource("http://feeds.feedburner.com/kriptovalyuta/novosti", "kriptovalyuta.com", SourceLanguage.RUS),
                    NewsSource("www.cryptomining.net/engine/rss.php", "kriptovalyuta.com", SourceLanguage.RUS)
            )
        }
    }
}