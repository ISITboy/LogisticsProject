package com.example.diploma.presentation.ui.tabs.profile

sealed class ProfileEvent {
    data object SignOut :ProfileEvent()
}