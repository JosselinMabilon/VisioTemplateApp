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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.example.visioglobe.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.visioglobe.ui.components.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val firstNameValue = remember { mutableStateOf("") }
                val lastNameValue = remember { mutableStateOf("") }
                val emailValue = remember { mutableStateOf("") }
                val phoneValue = remember { mutableStateOf("") }
                val passwordValue = remember { mutableStateOf("") }
                val confirmPasswordValue = remember { mutableStateOf("") }
                val passwordVisibility = remember { mutableStateOf(false) }
                val confirmPasswordVisibility = remember { mutableStateOf(false) }
                val focusManager = LocalFocusManager.current
                val focusRequester = remember { FocusRequester() }
                val scrollState = rememberScrollState()
                val errorState = viewModel.errorState.value
                val openDialog = remember { mutableStateOf(false) }

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
                            SetOutlinedField(
                                firstNameValue = firstNameValue,
                                lastNameValue = lastNameValue,
                                emailValue = emailValue,
                                phoneValue = phoneValue,
                                passwordValue = passwordValue,
                                confirmPasswordValue = confirmPasswordValue,
                                passwordVisibility = passwordVisibility,
                                confirmPasswordVisibility = confirmPasswordVisibility,
                                focusRequester = focusRequester,
                                focusManager = focusManager,
                                openDialog = openDialog
                            )
                            SetGeneratedPasswordButton(
                                passwordValue = passwordValue,
                                confirmPasswordValue = confirmPasswordValue
                            )
                            SetRegisterButton(
                                firstNameValue = firstNameValue,
                                lastNameValue = lastNameValue,
                                emailValue = emailValue,
                                phoneValue = phoneValue,
                                passwordValue = passwordValue,
                                confirmPasswordValue = confirmPasswordValue
                            )
                            SetBackToLoginText()
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
                .padding(top = VisioGlobeAppTheme.dims.padding_large)
        )
    }

    @Composable
    fun SetTitle() {
        Text(
            text = stringResource(id = R.string.register_sign_up),
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
    fun SetOutlinedField(
        firstNameValue: MutableState<String>,
        lastNameValue: MutableState<String>,
        emailValue: MutableState<String>,
        phoneValue: MutableState<String>,
        passwordValue: MutableState<String>,
        confirmPasswordValue: MutableState<String>,
        passwordVisibility: MutableState<Boolean>,
        confirmPasswordVisibility: MutableState<Boolean>,
        focusRequester: FocusRequester,
        focusManager: FocusManager,
        openDialog: MutableState<Boolean>
    ) {
        SetFirstNameField(firstNameValue, focusManager)
        SetDivider()
        SetLastNameField(lastNameValue, focusManager)
        SetDivider()
        SetEmailField(emailValue, focusManager)
        SetDivider()
        SetPhoneField(phoneValue, focusManager)
        SetDivider()
        SetPasswordField(
            passwordValue,
            passwordVisibility,
            focusRequester,
            focusManager,
            openDialog
        )
        SetDivider()
        SetConfirmPasswordField(
            confirmPasswordValue,
            confirmPasswordVisibility,
            focusRequester,
            focusManager
        )
    }

    @Composable
    fun SetFirstNameField(
        firstNameValue: MutableState<String>,
        focusManager: FocusManager
    ) {
        OutlinedTextField(
            value = firstNameValue.value,
            onValueChange = { firstNameValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.register_first_name),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onPrimary
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }

    @Composable
    fun SetLastNameField(
        lastNameValue: MutableState<String>,
        focusManager: FocusManager
    ) {
        OutlinedTextField(
            value = lastNameValue.value,
            onValueChange = { lastNameValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.register_last_name),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onPrimary
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }

    @Composable
    fun SetEmailField(
        emailValue: MutableState<String>,
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
                    text = stringResource(id = R.string.register_email_address),
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }

    @Composable
    fun SetPhoneField(
        phoneValue: MutableState<String>,
        focusManager: FocusManager
    ) {
        OutlinedTextField(
            value = phoneValue.value,
            onValueChange = { phoneValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.register_phone_number),
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onPrimary
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Phone,
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }

    @Composable
    fun SetPasswordField(
        passwordValue: MutableState<String>,
        passwordVisibility: MutableState<Boolean>,
        focusRequester: FocusRequester,
        focusManager: FocusManager,
        openDialog: MutableState<Boolean>
    ) {
        OutlinedTextField(
            value = passwordValue.value,
            onValueChange = { passwordValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.register_password),
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
                Row(modifier = Modifier, Arrangement.Center, Alignment.CenterVertically) {
                    FloatingActionButton(
                        modifier = Modifier.size(VisioGlobeAppTheme.dims.information_button),
                        onClick = { openDialog.value = true },
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "")
                    }
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.password_eye),
                            contentDescription = null,
                            tint = if (passwordVisibility.value) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                    if (openDialog.value) {
                        AlertDialog(
                            onDismissRequest = { openDialog.value = false },
                            title = {
                                Text(
                                    text = stringResource(R.string.info_password),
                                    style = MaterialTheme.typography.h5.copy(
                                        fontSize = VisioGlobeAppTheme.dims.text_large
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSecondary
                                )
                            },
                            text = {
                                Text(
                                    stringResource(R.string.info_password_text),
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = VisioGlobeAppTheme.dims.text_normal
                                    ),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            },
                            confirmButton = {},
                            dismissButton = {
                                TextButton(
                                    onClick = { openDialog.value = false }
                                ) {
                                    Text(
                                        stringResource(R.string.close_string),
                                        style = MaterialTheme.typography.h6.copy(
                                            fontSize = VisioGlobeAppTheme.dims.text_normal
                                        ),
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            },
                            modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_normal)
                        )
                    }
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
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
        )
    }

    @Composable
    fun SetConfirmPasswordField(
        confirmPasswordValue: MutableState<String>,
        confirmPasswordVisibility: MutableState<Boolean>,
        focusRequester: FocusRequester,
        focusManager: FocusManager
    ) {
        OutlinedTextField(
            value = confirmPasswordValue.value,
            onValueChange = { confirmPasswordValue.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.surface,
                unfocusedBorderColor = MaterialTheme.colors.surface
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.register_confirm_password),
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
                    confirmPasswordVisibility.value = !confirmPasswordVisibility.value
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.password_eye),
                        contentDescription = null,
                        tint = if (confirmPasswordVisibility.value) MaterialTheme.colors.primary else Color.Gray
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
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
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
        )
    }

    @Composable
    fun SetDivider() {
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
    fun SetGeneratedPasswordButton(
        passwordValue: MutableState<String>,
        confirmPasswordValue: MutableState<String>,
    ) {
        CustomOutlinedButton(
            onClick = {
                passwordValue.value = viewModel.generatePassword()
                confirmPasswordValue.value = passwordValue.value
            },
            buttonText = stringResource(R.string.register_generate_password),
            backgroundColor = Color.Transparent,
            borderColor = MaterialTheme.colors.onPrimary,
            buttonTextColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_small,
                bottom = VisioGlobeAppTheme.dims.padding_extra_small
            )
        )
    }

    @Composable
    fun SetRegisterButton(
        firstNameValue: MutableState<String>,
        lastNameValue: MutableState<String>,
        emailValue: MutableState<String>,
        phoneValue: MutableState<String>,
        passwordValue: MutableState<String>,
        confirmPasswordValue: MutableState<String>
    ) {
        CustomOutlinedButton(
            onClick = {
                viewModel.signUp(
                    firstNameValue.value,
                    lastNameValue.value,
                    emailValue.value,
                    phoneValue.value,
                    passwordValue.value,
                    confirmPasswordValue.value,
                    Activity()
                )
                viewModel.eventSignUpSuccess.observe(
                    viewLifecycleOwner
                ) { eventSignIn ->
                    if (eventSignIn == 1) {
                        findNavController().safeNavigate(
                            RegisterFragmentDirections.actionRegisterFragmentToConfirmRegisterFragment()
                        )
                    } else if (eventSignIn == 2) {
                        viewModel.eventSignUpReset()
                    }
                }
            },
            buttonText = stringResource(R.string.register_sign_up),
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_large)
        )
    }

    @Composable
    fun SetBackToLoginText() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_large,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_large
            )
        ) {
            Text(
                text = stringResource(id = R.string.register_back_to_login_page),
                style = MaterialTheme.typography.h6.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
            )
            Text(
                text = stringResource(id = R.string.login_sign_in),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal,
                    color = MaterialTheme.colors.primary
                ),
                modifier = Modifier
                    .clickable(onClick = {
                        findNavController().navigate(
                            R.id.action_registerFragment_to_loginFragment
                        )
                    })
                    .padding(start = VisioGlobeAppTheme.dims.padding_extra_small),
            )
        }
    }

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction)
        }
    }
}