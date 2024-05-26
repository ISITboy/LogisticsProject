package com.example.diploma.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.diploma.R

sealed class Destination(val route:String) {

    data object AuthorizationNavigation : Destination(route = "AuthorizationNavigation")
    data object SignInDestination:Destination(route = "SignInDestination")
    object SignUpDestination : Destination(route = "SignUpDestination"),ArgsDestination {
        override val data: String
            get() = "authData"

        override fun routeWithArgs() = "${route}/{${data}}"

        override val arguments: List<NamedNavArgument>
            get() = listOf(navArgument(data) { type = NavType.StringType })
    }

    data object TabsNavigation : Destination(route = "TabsNavigation")
    object MapKitDestination : Destination(route = "MapKitDestination"),ArgsDestination {
        override val data: String
            get() = "mapKitData"
        override fun routeWithArgs() = "${route}/{${data}}"
        override val arguments: List<NamedNavArgument>
            get() = listOf(navArgument(data) { type = NavType.StringType })
    }
    data object ManagerDestination : Destination(route = "ManagerDestination")
    data object ProfileDestination : Destination(route = "ProfileDestination")
    data object ManagerDetailsDestination : Destination(route = "ManagerDetailsDestination")

    data object SplashNavigation:Destination(route = "SplashNavigation")
    data object SplashDestination:Destination(route = "SplashDestination")
}

private interface ArgsDestination{
    val data:String
    fun routeWithArgs() :String
    val arguments : List<NamedNavArgument>
}