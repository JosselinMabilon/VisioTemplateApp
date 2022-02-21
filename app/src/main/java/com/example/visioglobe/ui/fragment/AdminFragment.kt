package com.example.visioglobe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.visioglobe.R
import com.example.visioglobe.ui.components.ShowError
import com.example.visioglobe.ui.components.ShowLoading
import com.example.visioglobe.ui.components.admin.ListUsers
import com.example.visioglobe.ui.components.incidentDeclaration.IncidentTextTitle
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.AdminViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        checkCurrentUser()

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    AdminContentView()
                }
            }
        }
    }

    @Composable
    private fun AdminContentView() {

        val listUser by viewModel.usersList.collectAsState(initial = emptyList())
        val errorState by remember(viewModel) { viewModel.errorState }
        val loadingState by remember(viewModel) { viewModel.loadingState }

        Column(
            Modifier
                .padding(top = VisioGlobeAppTheme.dims.padding_normal)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            if (errorState) {
                ShowError(getString(R.string.admin_fail_loading))
            } else {
                IncidentTextTitle(title = stringResource(R.string.string_users))
            }
            if (loadingState && listUser.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ShowLoading()
                }
            }
            ListUsers(listUser)
        }


    }

    private fun checkCurrentUser() {
        viewModel.checkUser(requireContext())
    }

}