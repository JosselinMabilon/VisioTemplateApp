package com.example.visioglobe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.ui.components.*

class ConfirmResetPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val scrollState = rememberScrollState()

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
                            SetResetPasswordMsg()
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
        )
    }

    @Composable
    fun SetTitle() {
        Text(
            text = stringResource(id = R.string.reset_password_string),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_extra_large,
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_large
            ),
            color = MaterialTheme.colors.onPrimary
        )
    }

    @Composable
    fun SetResetPasswordMsg() {
        Text(
            text = stringResource(id = R.string.reset_password_send_email_msg),
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_normal,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_large
            ),
            style = MaterialTheme.typography.h5.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun SetBackToLoginButton() {
        CustomOutlinedButton(
            onClick = { findNavController().navigate(R.id.action_confirmResetPasswordFragment_to_loginFragment) },
            buttonText = stringResource(R.string.login_sign_in),
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_large)
        )
    }
}