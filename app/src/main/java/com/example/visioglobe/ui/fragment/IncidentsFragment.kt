package com.example.visioglobe.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.visioglobe.R
import com.example.visioglobe.ui.components.ShowError
import com.example.visioglobe.ui.components.ShowLoading
import com.example.visioglobe.ui.components.incident.ListIncidents
import com.example.visioglobe.ui.components.incidentDeclaration.IncidentTextTitle
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidentsFragment : Fragment() {

    private val viewModel: IncidentsViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            )
            { isGranted: Boolean ->
                if (isGranted) {
                    findNavController().navigate(R.id.action_incidentsFragment_to_cameraFragment)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.camera_permission_denied),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    Surface(color = MaterialTheme.colors.surface) {
                        IncidentViewContent()
                    }
                }
            }
        }
    }

    @Composable
    fun IncidentViewContent() {

        val listIncidents by viewModel.incidentList.collectAsState(initial = emptyList())
        val errorState by remember(viewModel) { viewModel.errorState }
        val loadingState by remember(viewModel) { viewModel.loadingState }

        Scaffold(
            Modifier.padding(VisioGlobeAppTheme.dims.padding_small),
            floatingActionButton = { FabActionButton() },
            content = {
                Column(
                    Modifier
                        .padding(top = VisioGlobeAppTheme.dims.padding_normal)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    if (errorState) {
                        ShowError(getString(R.string.incidents_fail_loading))
                    } else {
                        IncidentTextTitle(title = stringResource(R.string.string_incidents))
                    }
                    if (loadingState && listIncidents.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ShowLoading()
                        }
                    }
                    ListIncidents(listIncidents, viewModel, findNavController())
                }
            })


    }

    @Composable
    fun FabActionButton() {
        FloatingActionButton(
            modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_large),
            onClick = {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            elevation = FloatingActionButtonDefaults.elevation(VisioGlobeAppTheme.dims.padding_small),
            backgroundColor = MaterialTheme.colors.primary
        )
        {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_photo_content_desc)
            )
        }
    }
}
