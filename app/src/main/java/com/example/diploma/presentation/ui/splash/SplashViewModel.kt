package com.example.diploma.presentation.ui.splash

import androidx.lifecycle.ViewModel
import com.example.diploma.domain.usecase.firebase.AuthUseCase
import com.example.diploma.presentation.mainActivity.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
):ViewModel() {

    fun onAppStart(openAndPopUp: (NavigationState) -> Unit) {
        if (authUseCase.hasUser()) openAndPopUp(NavigationState.TabsNavigation)
        else openAndPopUp(NavigationState.AuthorizationNavigation)
    }

    companion object{
        const val Tag = "SplashViewModel"
    }
}