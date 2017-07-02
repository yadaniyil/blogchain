package com.yadaniil.app.cryptomarket.di.module

import android.os.Environment
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yadaniil.app.cryptomarket.data.api.CoinMarketCapService
import com.yadaniil.app.cryptomarket.data.api.CryptoCompareService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetModule {
    @Singleton
    @Provides
    @Named("cached")
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cache = Cache(Environment.getDownloadCacheDirectory(), 10 * 1024 * 1024)
        val clientBuilder = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .cache(cache)
        clientBuilder.interceptors().add(loggingInterceptor)
        return clientBuilder.build()
    }

    @Singleton
    @Provides
    @Named("non_cached")
    fun provideNonCachedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient()
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, @Named("cached") client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    @Provides
    @Singleton
    fun provideCoinMarketCapService(builder: Retrofit.Builder): CoinMarketCapService {
        return builder.baseUrl(COIN_MARKET_CAP_URL)
                .build()
                .create(CoinMarketCapService::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoCompareService(builder: Retrofit.Builder): CryptoCompareService {
        return builder.baseUrl(CRYPTO_COMPARE_URL)
                .build()
                .create(CryptoCompareService::class.java)
    }

    companion object {
        val COIN_MARKET_CAP_URL = "https://api.coinmarketcap.com/v1/"
        val CRYPTO_COMPARE_URL = "https://www.cryptocompare.com/"
    }
}