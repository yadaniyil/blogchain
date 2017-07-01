package com.yadaniil.app.cryptomarket.main

import com.yadaniil.app.cryptomarket.di.scope.ActivityScope
import dagger.Module
import dagger.Provides


@Module
class MainModule(private val view: IMainView) {

    @Provides
    @ActivityScope
    internal fun provideView(): IMainView {
        return view
    }
}