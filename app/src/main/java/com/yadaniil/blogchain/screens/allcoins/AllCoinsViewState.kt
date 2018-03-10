package com.yadaniil.blogchain.screens.allcoins


/**
 * Created by danielyakovlev on 3/6/18.
 */

sealed class AllCoinsViewState
class ProgressBarLoadingViewState : AllCoinsViewState()
class LoadingErrorViewState : AllCoinsViewState()
class SwipeRefreshLoadingViewState : AllCoinsViewState()
class StopSwipeRefreshLoadingViewState : AllCoinsViewState()
class CoinsShowingViewState : AllCoinsViewState()