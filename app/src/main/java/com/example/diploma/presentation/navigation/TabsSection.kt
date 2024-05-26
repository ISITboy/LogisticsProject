package com.example.diploma.presentation.navigation

import androidx.annotation.StringRes

import com.example.diploma.R

enum class TabsSection(
    @StringRes val title: Int,
    val icon: Int,
    val route: String
) {
    MAP(title = R.string.map_tab, icon = R.drawable.ic_map_24, route = Destination.MapKitDestination.routeWithArgs()),
    MANAGER(title = R.string.manager_tab, icon = R.drawable.ic_manager_24, route = Destination.ManagerDestination.route),
    PROFILE(title = R.string.account_tab, icon = R.drawable.ic_account_circle_24, route = Destination.ProfileDestination.route)
}

val tabRowScreens = listOf(
    TabsSection.MANAGER,
    TabsSection.MAP,
    TabsSection.PROFILE,
)