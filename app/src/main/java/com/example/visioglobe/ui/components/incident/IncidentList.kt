package com.example.visioglobe.ui.components.incident

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.ui.fragment.IncidentsFragmentDirections
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentsViewModel


@Composable
fun ListIncidents(
    incidents: List<Incident>,
    viewModel: IncidentsViewModel,
    navController: NavController
) {
    val listState = rememberLazyListState()

    LazyColumn(Modifier.padding(VisioGlobeAppTheme.dims.padding_normal), state = listState) {
        items(
            incidents
        ) { incident ->
            IncidentListItem(incident, viewModel, navController)
        }
    }
}

@Composable
fun IncidentListItem(
    incident: Incident,
    viewModel: IncidentsViewModel,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(VisioGlobeAppTheme.dims.padding_small),
        modifier = Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_small)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(VisioGlobeAppTheme.dims.padding_small)
                .clickable {
                    navController.navigate(
                        IncidentsFragmentDirections.actionIncidentsFragmentToIncidentDetailFragment(
                            incident
                        )
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(VisioGlobeAppTheme.dims.padding_small)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = incident.title,
                    style = MaterialTheme.typography.h6.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                    color = MaterialTheme.colors.primary,
                    maxLines = 1
                )
                Text(
                    text = viewModel.mapToDateToToString(incident.time),
                    style = MaterialTheme.typography.caption.copy(fontSize = VisioGlobeAppTheme.dims.text_normal)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .offset(x = -(VisioGlobeAppTheme.dims.padding_normal))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = stringResource(R.string.add_photo_content_desc),
                    modifier = Modifier
                        .padding(end = VisioGlobeAppTheme.dims.padding_small),
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}