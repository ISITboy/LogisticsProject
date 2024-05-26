package com.example.diploma.presentation.ui.tabs.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.diploma.R
import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.RealtimeState
import com.example.diploma.presentation.mainActivity.NavigationState
import com.example.diploma.presentation.ui.authorization.components.EmailInputLayout
import com.example.diploma.presentation.ui.authorization.signup.NameInputLayout
import com.example.diploma.presentation.ui.tabs.ProgressIndicator

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onEvent: (NavigationState) -> Unit
) {

    val state = viewModel.getSate.collectAsState().value
    val showProgressIndicator = viewModel.statusProgressIndicator.collectAsState()
    val email = viewModel.email.collectAsState()
    val invalidEmail = remember { mutableStateOf(false) }
    val name = viewModel.name.collectAsState()
    val organization = viewModel.organization.collectAsState()
    val enabledFields = viewModel.enabledFields.collectAsState().value
    val actionButton = viewModel.actionButton.collectAsState().value

    LaunchedEffect(key1 = state) {
        when (state) {
            is RealtimeState.Success -> {
                viewModel.updateEmail(state.user.email)
                viewModel.updateName(state.user.name)
                viewModel.updateOrganization(state.user.organization)
                viewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
            }

            is RealtimeState.Error -> {
                viewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
            }

            RealtimeState.Loading -> {
                Log.d(ProfileViewModel.Tag,"RealtimeState.Loading $state")
                viewModel.changeStatusProgressIndicator(ProgressIndicator.SHOW)
            }

            RealtimeState.Off -> Unit
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBarLayout(
                modifier = Modifier.fillMaxWidth(),
                onSignOut = {
                    viewModel.signOut()
                    onEvent(NavigationState.SplashNavigation)
                }
            )
            NameInputLayout(
                name = name,
                enabled = enabledFields,
                onChangeSurname = { viewModel.updateName(it) })
            Spacer(modifier = Modifier.height(15.dp))
            EmailInputLayout(
                email = email,
                invalid = invalidEmail,
                enabled = enabledFields,
                onChangeEmail = { viewModel.updateEmail(it) })
            Spacer(modifier = Modifier.height(15.dp))
            OrganizationInputLayout(
                organization = organization,
                enabled = enabledFields,
                onChangeOrganization = { viewModel.updateOrganization(it) })


            if (actionButton) EditButton(onEditClick = {
                viewModel.onEvent(ProfileState.EditState)
            })
            else SaveButton(onSaveClick = {
                viewModel.onEvent(
                    ProfileState.SaveState(
                        User.Base(
                            id = viewModel.getUserId(),
                            email = viewModel.email.value,
                            name = viewModel.name.value,
                            organization = viewModel.organization.value
                        )
                    )
                )
            })
        }
        if (showProgressIndicator.value == ProgressIndicator.SHOW) {
            Log.d(ProfileViewModel.Tag,"showProgressIndicator ${showProgressIndicator.value}")
            CircularProgressIndicator(
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.TopStart)
                    .padding(top = 15.dp, end = 5.dp)
            )
        }
    }
}

@Composable
fun OrganizationInputLayout(organization: State<String>, enabled: Boolean = true, onChangeOrganization:(String)->Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(0.8f),
        value = organization.value,
        onValueChange = { onChangeOrganization(it) },
        label = { if(enabled)Text(text = stringResource(id = R.string.text_input_organization)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        enabled = enabled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarLayout(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text("Профиль") },
        actions = {
            IconButton(onClick = onSignOut) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null
                )
            }
        }
    )
}