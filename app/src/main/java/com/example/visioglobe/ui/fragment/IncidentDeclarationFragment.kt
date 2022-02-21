package com.example.visioglobe.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.visioglobe.R
import com.example.visioglobe.network.DataState
import com.example.visioglobe.ui.components.CustomOutlinedButton
import com.example.visioglobe.ui.components.incidentDeclaration.*
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentDeclarationViewModel
import com.example.visioglobe.viewmodel.IncidentDeclarationViewModel.Companion.NO_ERROR
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidentDeclarationFragment : Fragment() {

    val viewModel: IncidentDeclarationViewModel by viewModels()
    private lateinit var supportFragmentManager: FragmentManager
    private val safeArgs: IncidentDeclarationFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        val furnitureArgument = safeArgs.furniture
        val furnitureListArgument = safeArgs.furnitureList

        supportFragmentManager = this.childFragmentManager

        viewModel.getUserInfo(requireContext())
        viewModel.setFurnitureSelected(furnitureArgument)
        viewModel.setFurnitureList(furnitureListArgument)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.initBackPressedState()
        }

        return ComposeView(requireContext()).apply {
            setContent {
                VisioGlobeAppTheme {
                    ContentView()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ContentView() {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(VisioGlobeAppTheme.dims.padding_normal),
            verticalArrangement = Arrangement.spacedBy(VisioGlobeAppTheme.dims.padding_small)
        ) {

            IncidentTextTitle(getString(R.string.incident_declaration_title))

            Column(
                modifier = Modifier.padding(
                    bottom = VisioGlobeAppTheme.dims.padding_small,
                    start = VisioGlobeAppTheme.dims.padding_small
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserSection()

                GeneralSection()

                IncidentSection()

                DescriptionSection()

                ErrorText()

                CreateIncidentButton()
            }
            NavigateOnSuccess()
            HandleBackPress()
        }
    }

    @Composable
    private fun UserSection() {
        val reporterName: String by viewModel.reporterName.observeAsState("")
        val reporterFirstname: String by viewModel.reporterFirstname.observeAsState("")
        val email: String by viewModel.email.observeAsState("")
        val phoneNumber: String by viewModel.phoneNumber.observeAsState("")

        IncidentSectionContainer {
            IncidentHeaderRow(
                stringResource(R.string.incident_declaration_user_profile),
                Icons.Default.AccountCircle
            )
            Row(
                modifier = Modifier.fillMaxWidth(), Arrangement.Center
            ) {
                IncidentTextField(
                    placeholder = getString(R.string.field_name),
                    modifier = Modifier.fillMaxWidth(MIDDLE_FRACTION),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    value = reporterName,
                    onValueChange = { viewModel.onReporterNameChange(it) }
                )
                IncidentTextField(
                    placeholder = getString(R.string.field_firstname),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    value = reporterFirstname,
                    onValueChange = { viewModel.onReporterFirstnameChange(it) }
                )
            }
            IncidentTextField(
                placeholder = getString(R.string.field_email),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                required = false
            )
            IncidentTextField(
                placeholder = getString(R.string.field_phone_number),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                value = phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChange(it) },
                required = false
            )
            Text(
                text = getString(R.string.incident_declaration_field_required),
                style = MaterialTheme.typography.caption
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun GeneralSection() {
        val site: String by viewModel.site.observeAsState("")
        val location: String by viewModel.location.observeAsState("")
        val title: String by viewModel.title.observeAsState("")

        IncidentSectionContainer {
            IncidentHeaderRow(
                stringResource(R.string.incident_declaration_general),
                Icons.Default.EditLocationAlt
            )
            IncidentTextField(
                placeholder = getString(R.string.field_title),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = title,
                onValueChange = { viewModel.onTitleChange(it) }
            )
            IncidentTextField(
                placeholder = getString(R.string.field_site),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = site,
                onValueChange = { viewModel.onSiteChange(it) }
            )
            IncidentTextField(
                placeholder = getString(R.string.field_location),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = location,
                onValueChange = { viewModel.onLocationChange(it) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(), Arrangement.Center
            ) {
                IncidentDateField(
                    supportFragmentManager = supportFragmentManager,
                    viewModel
                )
                IncidentTimeField(
                    supportFragmentManager = supportFragmentManager,
                    viewModel
                )
            }
            Text(
                text = getString(R.string.incident_declaration_field_required),
                style = MaterialTheme.typography.caption
            )
        }
    }

    @Composable
    private fun IncidentSection() {
        val openDialog = remember { mutableStateOf(false) }

        IncidentSectionContainer {
            IncidentHeaderRow(
                stringResource(R.string.incident_declaration_damaged_element),
                Icons.Default.Handyman
            )

            Row(modifier = Modifier.fillMaxWidth(), Arrangement.Center) {
                DropDownMenu(viewModel)
                FloatingActionButton(
                    modifier = Modifier.size(30.dp),
                    onClick = { openDialog.value = true },
                    backgroundColor = MaterialTheme.colors.background
                )
                {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "")
                }
            }

            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    buttons = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            TextButton(
                                onClick = { openDialog.value = false },
                                modifier = Modifier
                                    .padding(
                                        bottom = VisioGlobeAppTheme.dims.padding_normal,
                                        end = VisioGlobeAppTheme.dims.padding_normal
                                    )
                                    .align(Alignment.End)
                            ) {
                                Text(
                                    text = getString(R.string.dismiss_string),
                                    style = MaterialTheme.typography.button.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                    },
                    text = {
                        Text(
                            text = getString(R.string.incident_declaration_furniture_information_popup),
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun DescriptionSection() {
        val description: String by viewModel.description.observeAsState("")

        IncidentSectionContainer {
            IncidentHeaderRow(
                stringResource(R.string.incident_declaration_description),
                Icons.Default.Description
            )

            IncidentTextField(
                placeholder = getString(R.string.field_description),
                maxLines = MAX_LINES,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(VisioGlobeAppTheme.dims.text_field_desc_incident_height),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                required = false
            )
        }
    }

    @Composable
    private fun ErrorText() {
        val context = LocalContext.current

        if (viewModel.errorResId.value != NO_ERROR) {
            Text(
                text = context.getString(viewModel.errorResId.value),
                modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_normal),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal
                ),
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun CreateIncidentButton() {
        CustomOutlinedButton(
            onClick = {
                viewModel.createIncident()
            },
            buttonText = stringResource(R.string.create),
            modifier = Modifier.padding(top = VisioGlobeAppTheme.dims.padding_small)
        )
    }

    @Composable
    private fun NavigateOnSuccess() {
        val context = LocalContext.current
        val incidentDeclarationState: DataState<DocumentReference> by viewModel.incidentDeclarationState

        when (incidentDeclarationState) {
            is DataState.Success -> {
                navigateToDeclarationSuccess((incidentDeclarationState as DataState.Success<DocumentReference>).data)
            }
            is DataState.Error -> Toast.makeText(
                context,
                stringResource(R.string.incident_declaration_save_fail),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Composable
    private fun HandleBackPress() {
        if (viewModel.backPressedState.value) {
            BackPressAlertDialog(
                getString(R.string.string_exit),
                stringResource(R.string.incident_declaration_back_press_dialog_leave),
                findNavController(),
                viewModel
            )
        }
    }

    private fun navigateToDeclarationSuccess(documentReference: DocumentReference) {
        val arg = documentReference.id
        val action =
            IncidentDeclarationFragmentDirections.actionIncidentDeclarationFragmentToIncidentDeclarationSuccessFragment(
                arg
            )
        findNavController().navigate(action)
    }


    companion object {
        const val MIDDLE_FRACTION = 0.5f
        const val MAX_LINES = 5
    }
}