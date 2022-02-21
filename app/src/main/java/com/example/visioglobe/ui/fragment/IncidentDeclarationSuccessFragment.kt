package com.example.visioglobe.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.visioglobe.R
import com.example.visioglobe.ui.components.CustomOutlinedButton
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidentDeclarationSuccessFragment : Fragment() {

    private val args: IncidentDeclarationSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val furnitureArgument = args.documentReference

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    ContentView(furnitureArgument)
                }
            }
        }
    }

    @Composable
    private fun ContentView(furnitureArgument: String?) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_normal)
        ) {

            Icon(
                tint = MaterialTheme.colors.primary,
                imageVector = Icons.Default.CheckCircle,
                modifier = Modifier
                    .size(VisioGlobeAppTheme.dims.obs_logo_width)
                    .padding(bottom = VisioGlobeAppTheme.dims.padding_extra_large),
                contentDescription = ""
            )

            Text(
                getString(R.string.incident_declaration_success_thanks_report),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Text(
                getString(R.string.incident_declaration_success_reference, furnitureArgument),
                fontWeight = FontWeight.Thin,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_extra_large)
            )

            CustomOutlinedButton(
                onClick = { navigateToIncidentsList() },
                buttonText = getString(R.string.incident_declaration_success_see_incidents),
                buttonTextColor = MaterialTheme.colors.primary,
                backgroundColor = Color.Transparent,
                borderWidth = VisioGlobeAppTheme.dims.border_width_large
            )
        }
    }

    private fun navigateToIncidentsList() {
        val action =
            IncidentDeclarationSuccessFragmentDirections.actionIncidentDeclarationSuccessFragmentToIncidentsFragment()
        findNavController().navigate(action)
    }
}

@Preview("Dark Theme", locale = "fr-rFR", device = Devices.PIXEL_C)
@Composable
fun IncidentDeclarationSuccessFragmentDarkTheme() {
    VisioGlobeAppTheme(darkTheme = true) {
        val context = LocalContext.current
        Surface {
            Text(
                context.getString(
                    R.string.incident_declaration_success_reference,
                    "hg5hjGts64G4jk3tHyb6"
                ),
                fontWeight = FontWeight.Thin,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_extra_large)
            )
        }
    }
}

@Preview("Light Theme", locale = "fr-rFR", device = Devices.PIXEL_C)
@Composable
fun IncidentDeclarationSuccessFragmentLightTheme() {
    VisioGlobeAppTheme(darkTheme = false) {
        val context = LocalContext.current
        Surface {
            Text(
                context.getString(
                    R.string.incident_declaration_success_reference,
                    "hg5hjGts64G4jk3tHyb6"
                ),
                fontWeight = FontWeight.Thin,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier =
                Modifier.padding(bottom = VisioGlobeAppTheme.dims.padding_extra_large)
            )
        }
    }
}
