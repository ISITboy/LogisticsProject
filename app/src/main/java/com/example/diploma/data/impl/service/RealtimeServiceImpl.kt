package com.example.diploma.data.impl.service

import android.util.Log
import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.RealtimeState
import com.example.diploma.domain.service.AccountService
import com.example.diploma.domain.service.RealtimeService
import com.example.diploma.presentation.ui.tabs.profile.ProfileViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class RealtimeServiceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: AccountService
) : RealtimeService {

    private val referencesUser = firebaseDatabase.getReference(Reference.USER.value)
    private val state = MutableStateFlow<RealtimeState>(RealtimeState.Off)

    override fun getState() = state

    override suspend fun createUser(user: User.Base) {
        val userReference = referencesUser.child(user.id)
        userReference.setValue(user)
            .addOnSuccessListener {
                state.value = RealtimeState.Success()
            }
            .addOnFailureListener {
                state.value = RealtimeState.Error(it.message.toString())
            }
    }


    override suspend fun updateUser(user: User.Base) {
        state.value = RealtimeState.Loading
        val userReference = referencesUser.child(auth.currentUserId)

        auth.updateEmail(user.email)
        val updates =  hashMapOf<String, Any>(
            "name" to user.name,
            "email" to user.email,
            "organization" to user.organization
        )

        userReference.updateChildren(updates)
            .addOnSuccessListener {
                state.value = RealtimeState.Success()
            }
            .addOnFailureListener { error ->
                state.value = RealtimeState.Error(error.message.toString())
            }

    }

    override suspend fun readUser() {
        state.value = RealtimeState.Loading
        val userReference = referencesUser.child(auth.currentUserId)
        userReference.get()
            .addOnSuccessListener {
                val retrievedUser = it.getValue(User.Base::class.java)
                retrievedUser?.let {

                    state.value = RealtimeState.Success(user = retrievedUser)
                }
            }
            .addOnFailureListener {
                state.value = RealtimeState.Error(it.message.toString())
            }
    }

}