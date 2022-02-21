package com.example.visioglobe.ui.components.incidentDeclaration

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentDeclarationViewModel

@Composable
fun BackPressAlertDialog(
    title: String,
    content: String,
    navController: NavController,
    viewModel: IncidentDeclarationViewModel
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                viewModel.resetBackPressedState()
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_large
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary
                )
            },
            text = {
                Text(
                    content,
                    style = MaterialTheme.typography.h6.copy(
                        fontSize = VisioGlobeAppTheme.dims.text_normal
                    ),
                    color = MaterialTheme.colors.onSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        viewModel.resetBackPressedState()
                        navController.navigateUp()
                    }
                ) {
                    Text(
                        stringResource(R.string.confirm_string),
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = VisioGlobeAppTheme.dims.text_normal
                        ),
                        color = MaterialTheme.colors.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        viewModel.resetBackPressedState()
                    }
                ) {
                    Text(
                        stringResource(R.string.dismiss_string),
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
