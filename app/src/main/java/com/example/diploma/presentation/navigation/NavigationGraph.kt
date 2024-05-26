package com.example.diploma.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.diploma.presentation.mainActivity.MainViewModel
import com.example.diploma.presentation.mainActivity.NavigationState
import com.example.diploma.presentation.navigation.Destination.*
import com.example.diploma.presentation.ui.authorization.signin.SignInEvent
import com.example.diploma.presentation.ui.authorization.signin.SignInScreen
import com.example.diploma.presentation.ui.authorization.signin.SignInViewModel
import com.example.diploma.presentation.ui.authorization.signup.SignUpEvent
import com.example.diploma.presentation.ui.authorization.signup.SignUpScreen
import com.example.diploma.presentation.ui.authorization.signup.SignUpViewModel
import com.example.diploma.presentation.ui.splash.SplashScreen
import com.example.diploma.presentation.ui.splash.SplashViewModel
import com.example.diploma.presentation.ui.tabs.TabsNavigator
import com.example.diploma.presentation.ui.tabs.manager.detailsManager.DetailsManagerScreen
import com.example.diploma.presentation.ui.tabs.manager.detailsManager.DetailsManagerViewModel
import com.example.diploma.presentation.ui.tabs.mapkit.MapKitViewModel

@Composable
fun NavigationGraph(
    startDestination: String,
    viewModel: MainViewModel,
) {
    val navGraphState = rememberAppState()

    NavHost(
        navController = navGraphState.navController,
        startDestination = startDestination
    ) {
        authorizationGraph(appState = navGraphState, viewModel = viewModel)
        tabsGraph(appState = navGraphState,viewModel = viewModel)
        splashScreen(viewModel = viewModel)
    }
}


fun NavGraphBuilder.splashScreen(viewModel: MainViewModel){
    navigation(
        route = SplashNavigation.route,
        startDestination = SplashDestination.route
    ) {
        composable(route = SplashDestination.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(viewModel = splashViewModel, onNavigationEvent = viewModel::onNavigationEvent)
        }
    }
}

fun NavGraphBuilder.authorizationGraph(appState: NavGraphState, viewModel: MainViewModel){
    navigation(
        route = AuthorizationNavigation.route,
        startDestination = SignInDestination.route
    ){
        composable(route = SignInDestination.route){
            val signInViewModel : SignInViewModel = hiltViewModel()
            SignInScreen(viewModel = signInViewModel){
                when(it) {
                    is SignInEvent.GoToSignUpScreen -> appState.navigate("${SignUpDestination.route}/${it.email}")
                    SignInEvent.SignIn -> viewModel.onNavigationEvent(NavigationState.TabsNavigation)
                }
            }
        }
        composable(
            route = SignUpDestination.routeWithArgs(),
            arguments = SignUpDestination.arguments
        ){navBackStackEntry->
            val signUpViewModel:SignUpViewModel= hiltViewModel()
            val emailType =
                navBackStackEntry.arguments?.getString(SignUpDestination.data)
            SignUpScreen(viewModel = signUpViewModel,emailType = emailType ?: "empty"){
                when(it) {
                    SignUpEvent.BackToSignIpScreen -> appState.popUp()
                    SignUpEvent.SignUp -> appState.popUp()
                }
            }
        }
    }
}

fun NavGraphBuilder.tabsGraph(appState: NavGraphState,viewModel: MainViewModel){
    navigation(
        route = TabsNavigation.route,
        startDestination = MapKitDestination.route
    ){
        composable(route = MapKitDestination.route){
            TabsNavigator(viewModel=viewModel){
                appState.navigate(it.route)
            }
        }
        composable(route = ManagerDetailsDestination.route){
            val detailsManagerViewModel:DetailsManagerViewModel = hiltViewModel()
            DetailsManagerScreen(viewModel = detailsManagerViewModel){
                appState.popUp()
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        NavGraphState(navController)
    }