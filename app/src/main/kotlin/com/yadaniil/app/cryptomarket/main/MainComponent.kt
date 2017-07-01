package com.yadaniil.app.cryptomarket.main

import com.yadaniil.app.cryptomarket.di.component.ApplicationComponent
import com.yadaniil.app.cryptomarket.di.scope.ActivityScope
import dagger.Component


@ActivityScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface MainComponent {
    fun inject(activity: MainActivity)
}