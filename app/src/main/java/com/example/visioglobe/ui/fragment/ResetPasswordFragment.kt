package com.example.visioglobe.ui.fragment

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.visioglobe.ui.components.*

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val emailValue = remember { mutableStateOf("") }
                val focusManager = LocalFocusManager.current
                val scrollState = rememberScrollState()
                val errorState = viewModel.errorState.value

                VisioGlobeAppTheme {
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
                            SetEmailMsg()
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
                            SetEmailOutlinedField(
                                emailValue = emailValue,
                                focusManager = focusManager
                            )
                            SetNextButton(emailValue = emailValue)
                            SetDivider()
                            SetBackToLoginButton()
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
            text = stringResource(id = R.string.reset_password_string),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_extra_large,
                bottom = VisioGlobeAppTheme.dims.padding_extra_large
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_large
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }

    @Composable
    fun SetEmailMsg() {
        Text(
            text = stringResource(id = R.string.reset_password_msg),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_normal,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun SetEmailOutlinedField(
        emailValue: MutableState<String>,
        focusManager: FocusManager,
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
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
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
    }

    @Composable
    fun SetNextButton(
        emailValue: MutableState<String>,
    ) {
        CustomOutlinedButton(
            onClick = {
                viewModel.resetPassword(emailValue.value)
                viewModel.eventResetPasswordSuccess.observe(
                    viewLifecycleOwner,
                    { eventResetPassword ->
                        if (eventResetPassword == 1) {
                            findNavController().navigate(R.id.action_resetPasswordFragment_to_confirmResetPasswordFragment)
                        } else if (eventResetPassword == 2) {
                            viewModel.eventResetPasswordReset()
                        }
                    })
            },
            buttonText = stringResource(R.string.string_next),
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
                text = "OR",
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
    fun SetBackToLoginButton() {
        CustomOutlinedButton(
            onClick = { findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment) },
            buttonText = stringResource(R.string.login_sign_in),
            buttonTextColor = MaterialTheme.colors.primary,
            backgroundColor = Color.Transparent,
            borderWidth = VisioGlobeAppTheme.dims.border_width_large,
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_zero)
        )
    }
}