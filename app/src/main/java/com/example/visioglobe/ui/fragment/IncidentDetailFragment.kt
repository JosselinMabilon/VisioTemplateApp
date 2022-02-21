package com.example.visioglobe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.annotation.ExperimentalCoilApi
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.ui.components.incident.*
import com.example.visioglobe.ui.components.incidentDeclaration.IncidentHeaderRow
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IncidentDetailFragment : Fragment() {

    private val viewModel: IncidentDetailViewModel by viewModels()
    private val safeArgs: IncidentDetailFragmentArgs by navArgs()

    @ExperimentalCoilApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val incident = safeArgs.incident
        viewModel.setSelectedIncident(incident)

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    IncidentDetailCard(incident)
                }
            }
        }
    }

    @ExperimentalCoilApi
    @Composable
    private fun IncidentDetailCard(incident: Incident) {

        val imagePath by viewModel.imagePath.collectAsState(initial = "")
        val incidentSelected by viewModel.selectedIncident.observeAsState(initial = incident)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VisioGlobeAppTheme.dims.padding_small)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IncidentHeaderBackButton(text = incidentSelected.title) { requireActivity().onBackPressed() }

            BoxIncidentImage(
                imagePath = imagePath,
                heightFraction = VisioGlobeAppTheme.dims.incident_image_detail_height_fraction,
                scale = ContentScale.Crop
            )

            IncidentDescription(incidentSelected)

            IncidentAuthor(incidentSelected)
        }
    }

    @Composable
    private fun IncidentDescription(incidentSelected: Incident) {
        IncidentDescContainer {
            IncidentDetailsRow(
                viewModel.dateFormatter.mapToDateString(incidentSelected.time),
                Icons.Default.CalendarToday,
                MaterialTheme.colors.primary
            )

            IncidentDetailsRow(incidentSelected.furniture.name, Icons.Default.Handyman)

            if (!incidentSelected.description.isNullOrEmpty()) {
                IncidentDetailsRow(incidentSelected.description!!, Icons.Default.Description)
            }
        }
    }

    @Composable
    private fun IncidentAuthor(incidentSelected: Incident) {
        IncidentUserProfileContainer {
            IncidentHeaderRow(
                stringResource(R.string.incident_declaration_user_profile),
                Icons.Default.AccountCircle
            )
            Row(
                modifier = Modifier
                    .padding(VisioGlobeAppTheme.dims.padding_small)
                    .fillMaxWidth(),
                Arrangement.Start
            ) {
                Text(
                    text = getString(R.string.field_name),
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_small))
                Text(
                    text = "${incidentSelected.reporterName} ${incidentSelected.reporterFirstname}",
                    style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_normal)
                )
            }

            incidentSelected.email?.let {
                Row(
                    modifier = Modifier
                        .padding(VisioGlobeAppTheme.dims.padding_small)
                        .fillMaxWidth(),
                    Arrangement.Start
                ) {
                    Text(
                        text = getString(R.string.field_email),
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = VisioGlobeAppTheme.dims.text_normal,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_small))
                    Text(
                        it,
                        style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_normal)
                    )
                }
            }
            incidentSelected.phoneNumber?.let {
                Row(
                    modifier = Modifier
                        .padding(VisioGlobeAppTheme.dims.padding_small)
                        .fillMaxWidth(),
                    Arrangement.Start
                ) {
                    Text(
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = VisioGlobeAppTheme.dims.text_normal
                        ),
                        text = getString(R.string.field_phone_number)
                    )
                    Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_small))
                    Text(
                        it,
                        style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                    )
                }
            }
        }
    }
}