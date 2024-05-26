package com.example.diploma.domain.models.state

import com.example.diploma.domain.models.User

sealed interface RealtimeState {
    data object Off : RealtimeState

    data class Error(val message:String) : RealtimeState
    data object Loading:RealtimeState

    data class Success(val user: User.Base = User.Base()) : RealtimeState

}