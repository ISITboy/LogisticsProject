package com.example.diploma.presentation.mainActivity

import com.example.diploma.presentation.navigation.Destination

sealed class NavigationState(val navigation: String) {
    data object AuthorizationNavigation :
        NavigationState(navigation = Destination.AuthorizationNavigation.route)
    data object TabsNavigation:NavigationState(navigation = Destination.TabsNavigation.route)
    data object SplashNavigation:NavigationState(navigation = Destination.SplashNavigation.route)
}