package com.example.diploma.presentation.ui.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.models.User
import com.example.diploma.domain.service.AccountService
import com.example.diploma.domain.service.RealtimeService
import com.example.diploma.presentation.ui.tabs.ProgressIndicator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseService: RealtimeService,
    private val accountService: AccountService
) : ViewModel() {

    init {
        readUser()
    }

    private val _enabledFields = MutableStateFlow(false)
    val enabledFields: StateFlow<Boolean> = _enabledFields

    private val _actionButton = MutableStateFlow(true)
    val actionButton :StateFlow<Boolean> = _actionButton

    private val _statusProgressIndicator = MutableStateFlow(ProgressIndicator.HIDE)
    val statusProgressIndicator: StateFlow<ProgressIndicator> =
        _statusProgressIndicator.asStateFlow()

    fun changeStatusProgressIndicator(status: ProgressIndicator) {
        _statusProgressIndicator.value = status
    }

    fun signOut() =viewModelScope.launch {
        accountService.signOut()
    }
    fun getUserId() = accountService.currentUserId

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _name = MutableStateFlow("")
    val name : StateFlow<String> = _name.asStateFlow()

    private val _organization = MutableStateFlow("")
    val organization : StateFlow<String> = _organization.asStateFlow()


    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
    fun updateOrganization(organization: String) {
        _organization.value = organization
    }

    fun updateName(newEmail: String) {
        _name.value = newEmail
    }

    private fun readUser()=viewModelScope.launch {
        firebaseService.readUser()
    }

    val getSate = firebaseService.getState()

    private fun updateUser(user: User.Base) = viewModelScope.launch {
        firebaseService.updateUser(user)
    }
    fun onEvent(profileState: ProfileState){
        when(profileState){
            ProfileState.EditState->{
                _actionButton.value = false
                _enabledFields.value = true
            }
            is ProfileState.SaveState->{
                _actionButton.value = true
                _enabledFields.value = false
                updateUser(profileState.user)
                readUser()
            }
        }
    }

    companion object {
        const val Tag = "ProfileViewModel"
    }

}