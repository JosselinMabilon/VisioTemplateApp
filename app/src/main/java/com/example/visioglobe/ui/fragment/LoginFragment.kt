package com.example.visioglobe.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.visioglobe.ui.components.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.userIsConnected()

                val isConnected = viewModel.isConnected.value
                val emailValue = remember { mutableStateOf("") }
                val passwordValue = remember { mutableStateOf("") }
                val passwordVisibility = remember { mutableStateOf(false) }
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                val errorState = viewModel.errorState.value
                val scrollState = rememberScrollState()

                VisioGlobeAppTheme {
                    if (isConnected) {
                        findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else {
                        Surface(color = MaterialTheme.colors.surface) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SetImage()
                                SetTitle()
                                if (errorState) {
                                    Text(
                                        text = viewModel.errorMessage.value,
                                        modifier = Modifier.padding(
                                            top = VisioGlobeAppTheme.dims.border_width_normal,
                                            start = VisioGlobeAppTheme.dims.border_width_normal,
                                            end = VisioGlobeAppTheme.dims.border_width_normal,
                                            bottom = VisioGlobeAppTheme.dims.border_width_large
                                        ),
                                        style = MaterialTheme.typography.h6.copy(
                                            fontSize = VisioGlobeAppTheme.dims.text_normal
                                        ),
                                        color = MaterialTheme.colors.error,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                SetOutlinedFields(
                                    emailValue,
                                    passwordValue,
                                    passwordVisibility,
                                    focusRequester,
                                    focusManager
                                )
                                SetForgotPassword()
                                SetLoginButton(emailValue, passwordValue)
                                SetDivider()
                                SetSignUpButton()
                                SetGuestButton()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SetImage() {
        Image(
            painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.obs_logo_white)
            else painterResource(id = R.drawable.obs_logo_dark),
            contentDescription = null,
            modifier = Modifier
                .width(VisioGlobeAppTheme.dims.obs_logo_width)
                .height(VisioGlobeAppTheme.dims.obs_logo_height)
                .padding(top = VisioGlobeAppTheme.dims.padding_normal)
        )
    }

    @Composable
    fun SetTitle() {
        Text(
            text = stringResource(id = R.string.general_services_string),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_large,
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_large
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }

    @Composable
    fun SetOutlinedFields(
        emailValue: MutableState<String>,
        passwordValue: MutableState<String>,
        passwordVisibility: MutableState<Boolean>,
        focusRequester: FocusRequester,
        focusManager: FocusManager
    ) {
        OutlinedTextField(
            value = emailValue.value,
            onValueChange = { emailValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.login_field),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onPrimary
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = VisioGlobeAppTheme.dims.padding_normal,
                    end = VisioGlobeAppTheme.dims.padding_normal
                )
                .border(
                    border = BorderStroke(
                        VisioGlobeAppTheme.dims.no_border_width,
                        color = MaterialTheme.colors.surface
                    )
                ),
            textStyle = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal,
                color = MaterialTheme.colors.onPrimary
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            )
        )
        Divider(
            color = MaterialTheme.colors.onSecondary,
            thickness = VisioGlobeAppTheme.dims.border_width_small,
            modifier = Modifier
                .padding(
                    top = VisioGlobeAppTheme.dims.padding_small,
                    start = VisioGlobeAppTheme.dims.padding_normal,
                    end = VisioGlobeAppTheme.dims.padding_normal,
                    bottom = VisioGlobeAppTheme.dims.padding_small
                )
        )
        OutlinedTextField(
            value = passwordValue.value,
            onValueChange = { passwordValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.password_field),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onPrimary
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Password,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility.value = !passwordVisibility.value
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.password_eye),
                        contentDescription = null,
                        tint = if (passwordVisibility.value) MaterialTheme.colors.primary else Color.Gray
                    )
                }
            },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        VisioGlobeAppTheme.dims.no_border_width,
                        color = MaterialTheme.colors.surface
                    )
                )
                .padding(
                    start = VisioGlobeAppTheme.dims.padding_normal,
                    end = VisioGlobeAppTheme.dims.padding_normal
                )
                .focusRequester(focusRequester = focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            textStyle = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal,
                color = MaterialTheme.colors.onPrimary
            ),
        )
    }

    @Composable
    fun SetForgotPassword() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_small,
                bottom = VisioGlobeAppTheme.dims.padding_small
            )
        ) {
            Text(
                text = stringResource(id = R.string.string_forgot_password),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal
                ),
                modifier = Modifier
                    .clickable(onClick = {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_resetPasswordFragment
                        )
                    })
                    .padding(start = VisioGlobeAppTheme.dims.padding_extra_small),
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.End
            )
        }
    }

    @Composable
    fun SetLoginButton(
        emailValue: MutableState<String>,
        passwordValue: MutableState<String>
    ) {
        CustomOutlinedButton(
            onClick = {
                viewModel.signIn(
                    emailValue.value,
                    passwordValue.value,
                    Activity()
                )
                viewModel.eventSignInSuccess.observe(
                    viewLifecycleOwner
                ) { eventSignIn ->
                    if (eventSignIn == 1) {
                        findNavController().safeNavigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        )
                    } else if (eventSignIn == 2) {
                        viewModel.eventSignInReset()
                    }
                }
            },
            buttonText = stringResource(R.string.login_sign_in),
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_large)
        )
    }

    @Composable
    fun SetDivider() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_normal,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_normal
            )
        ) {
            Divider(
                color = MaterialTheme.colors.onPrimary,
                thickness = VisioGlobeAppTheme.dims.border_width_normal,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .padding(
                        end = VisioGlobeAppTheme.dims.padding_normal,
                    )
            )
            Text(
                text = stringResource(R.string.string_or),
                style = MaterialTheme.typography.h5.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_large
                ),
                color = MaterialTheme.colors.primary
            )
            Divider(
                color = MaterialTheme.colors.onPrimary,
                thickness = VisioGlobeAppTheme.dims.border_width_normal,
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .padding(
                        start = VisioGlobeAppTheme.dims.padding_normal,
                    )
            )
        }
    }

    @Composable
    fun SetSignUpButton() {
        CustomOutlinedButton(
            onClick = { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) },
            buttonText = stringResource(R.string.register_sign_up),
            buttonTextColor = MaterialTheme.colors.primary,
            backgroundColor = Color.Transparent,
            borderWidth = VisioGlobeAppTheme.dims.border_width_large,
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_zero)
        )
    }

    @Composable
    fun SetGuestButton() {
        CustomOutlinedButton(
            onClick = { findNavController().navigate(R.id.action_loginFragment_to_homeFragment) },
            buttonText = stringResource(R.string.login_guest),
            buttonTextColor = MaterialTheme.colors.primary,
            backgroundColor = Color.Transparent,
            borderWidth = VisioGlobeAppTheme.dims.border_width_large,
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_normal)
        )
    }

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction)
        }
    }
}