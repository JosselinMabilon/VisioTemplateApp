package com.example.visioglobe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.ui.components.account.AccountHeader
import com.example.visioglobe.ui.components.account.LogOutDialog
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.getUserInfo()
                val scrollState = rememberScrollState()
                val userInfo = viewModel.userInfo.value

                VisioGlobeAppTheme {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MainContent(viewModel, findNavController(), userInfo)
                    }
                }
            }
        }
    }

    @Composable
    private fun MainContent(
        viewModel: AccountViewModel,
        navController: NavController,
        userInfo: User
    ) {
        val logOutState = viewModel.logOutState.value

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AccountHeader(userInfo)
            OutlinedButton(
                onClick = { viewModel.logOutChangeToTrue() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                shape = RectangleShape,
                border = BorderStroke(
                    width = VisioGlobeAppTheme.dims.border_width_normal,
                    color = MaterialTheme.colors.onSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = VisioGlobeAppTheme.dims.padding_normal,
                        start = VisioGlobeAppTheme.dims.padding_log_out,
                        end = VisioGlobeAppTheme.dims.padding_log_out
                    )
            ) {
                Text(
                    text = stringResource(R.string.log_out_string),
                    style = MaterialTheme.typography.button.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onSecondary,
                )
                if (logOutState) {
                    LogOutDialog(
                        title = stringResource(R.string.log_out_string),
                        content = stringResource(R.string.log_out_dialog_waning),
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
