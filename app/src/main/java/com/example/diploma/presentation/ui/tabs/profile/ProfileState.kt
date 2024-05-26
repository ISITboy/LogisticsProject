package com.example.diploma.presentation.ui.tabs.profile

import com.example.diploma.domain.models.User

sealed class ProfileState {
    data object EditState:ProfileState()
    data class SaveState(val user: User.Base) :ProfileState()
}
