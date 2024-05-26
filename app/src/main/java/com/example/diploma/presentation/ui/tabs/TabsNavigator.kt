package com.example.diploma.presentation.ui.tabs

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.diploma.presentation.mainActivity.MainViewModel
import com.example.diploma.presentation.navigation.Destination
import com.example.diploma.presentation.navigation.NavGraphState
import com.example.diploma.presentation.navigation.TabsSection
import com.example.diploma.presentation.navigation.rememberAppState
import com.example.diploma.presentation.navigation.tabRowScreens
import com.example.diploma.presentation.ui.splash.SplashViewModel
import com.example.diploma.presentation.ui.tabs.manager.ManagerScreen
import com.example.diploma.presentation.ui.tabs.manager.ManagerViewModel
import com.example.diploma.presentation.ui.tabs.mapkit.MapKitScreen
import com.example.diploma.presentation.ui.tabs.mapkit.MapKitViewModel
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.DetailsSheetViewModel
import com.example.diploma.presentation.ui.tabs.profile.ProfileScreen
import com.example.diploma.presentation.ui.tabs.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsNavigator(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onOpenDetailsManager: (Destination) -> Unit
) {
    val tabsScreens = remember { tabRowScreens }
    val navGraphState = rememberAppState()
    val backStackState by navGraphState.navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                allScreens = tabsScreens,
                onTabSelected = { newScreen ->
                    Log.d("micha","newScreen ${newScreen.route}")
                    if(newScreen == TabsSection.MAP) navGraphState.navigateSingleTopTo("${Destination.MapKitDestination.route}/{empty}")
                    else navGraphState.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = getCurrentScreen(backStackState?.destination)
            )
        }
    ) { paddingValues ->
        AppNavHost(
            paddingValues = paddingValues,
            appState = navGraphState,
            viewModel = viewModel
        ) {
            onOpenDetailsManager(Destination.ManagerDetailsDestination)
        }
    }
}

@Composable
fun AppNavHost(
    paddingValues: PaddingValues,
    appState: NavGraphState,
    viewModel: MainViewModel,
    clickOpenDetailsManager: () -> Unit
) {

    NavHost(
        navController = appState.navController,
        startDestination = Destination.MapKitDestination.routeWithArgs(),
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(
            route = Destination.MapKitDestination.routeWithArgs(),
            arguments = Destination.MapKitDestination.arguments
        ) { navBackStackEntry ->
            val mapViewModel: MapKitViewModel = hiltViewModel()
            val detailsVewModel: DetailsSheetViewModel = hiltViewModel()
            val points =
                navBackStackEntry.arguments?.getString(Destination.MapKitDestination.data)
            MapKitScreen(
                mainViewModel = viewModel,
                mapViewModel = mapViewModel,
                detailsVewModel = detailsVewModel,
                pointsForCreateRouts = points ?: "empty"
            )
        }
        composable(route = Destination.ProfileDestination.route) {
            val profileViewModel :ProfileViewModel = hiltViewModel()
            ProfileScreen(viewModel = profileViewModel,onEvent = viewModel::onNavigationEvent)
        }
        composable(
            route = Destination.ManagerDestination.route,
        ) {
            val managerViewModel: ManagerViewModel = hiltViewModel()
            ManagerScreen(
                viewModel = managerViewModel,
                clickOpenDetailsManager = clickOpenDetailsManager,
                createRoute = {route,popUp->
                    appState.navigateAndPopUp(route,popUp)
                }
            )
        }
    }
}

private fun getCurrentScreen(currentDestination: NavDestination?) =
    tabRowScreens.find { it.route == currentDestination?.route } ?: TabsSection.MAP