package com.example.diploma.presentation.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.diploma.presentation.navigation.TabsSection

@Composable
fun BottomNavBar(
    allScreens: List<TabsSection>,
    onTabSelected: (TabsSection) -> Unit,
    currentScreen: TabsSection,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 10.dp
    ) {
        allScreens.forEach{ screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onTabSelected(screen) },
                icon = { TabItem(title = screen.title, icon = screen.icon)},
                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = colorResource(id = R.color.selected_tab_icon),
//                    selectedTextColor = colorResource(id = R.color.selected_tab_icon),
//                    unselectedIconColor = colorResource(id = R.color.unselected_tab_icon),
//                    unselectedTextColor = colorResource(id = R.color.unselected_tab_icon),
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}

@Composable
fun TabItem(title:Int, icon:Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = stringResource(id = title), style = MaterialTheme.typography.labelSmall)
    }
}