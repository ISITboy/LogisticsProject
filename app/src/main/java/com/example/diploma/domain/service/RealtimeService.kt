package com.example.diploma.domain.service


import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.RealtimeState
import kotlinx.coroutines.flow.MutableStateFlow

interface RealtimeService {

    fun getState(): MutableStateFlow<RealtimeState>
    suspend fun createUser(user: User.Base)
    suspend fun updateUser(user: User.Base)
    suspend fun readUser()
}