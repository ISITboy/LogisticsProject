package com.example.diploma.presentation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Stable
class NavGraphState(val navController: NavHostController) {
    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }
    fun navigateSingleTopTo(route: String) =
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }


    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) {
                inclusive = true
                saveState = true
            }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

//popUp(): Этот метод используется для возвращения на предыдущий экран в стеке навигации. Он вызывает popBackStack() для NavController, что приводит к удалению верхнего фрагмента из стека навигации и переходу на предыдущий фрагмент.
//
//navigate(route: String): Этот метод используется для перехода к определенному маршруту (destination) в приложении. Он вызывает navigate(route) для NavController, что приводит к переходу к указанному маршруту. Опция launchSingleTop = true указывает, что если указанный маршрут уже был открыт на вершине стека навигации, то новый экземпляр этого маршрута не будет создан, а вместо этого будет использован существующий.
//
//navigateAndPopUp(route: String, popUp: String): Этот метод выполняет две операции: навигацию к указанному маршруту и удаление всех фрагментов из стека навигации до указанного фрагмента (включительно). Он вызывает navigate(route) для перехода к указанному маршруту и использует popUpTo(popUp) для удаления фрагментов из стека навигации до указанного фрагмента popUp, включительно. Опция inclusive = true указывает на то, что фрагмент popUp также должен быть удален из стека навигации.
//
//clearAndNavigate(route: String): Этот метод очищает весь стек навигации и переходит к указанному маршруту. Он вызывает navigate(route) для перехода к указанному маршруту и использует popUpTo(0) для удаления всех фрагментов из стека навигации. Опция inclusive = true указывает на то, что все фрагменты в стеке, включая текущий, будут удалены перед переходом к указанному маршруту.