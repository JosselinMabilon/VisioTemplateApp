package com.example.visioglobe.ui.components.incidentDeclaration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.extension.percentage
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentDeclarationViewModel

@Composable
fun DropDownMenu(
    viewModel: IncidentDeclarationViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    val listFurniture: List<Furniture> = remember { viewModel.listFurniture }
    val furnitureSelected: Furniture by viewModel.selectedFurniture.observeAsState(viewModel.listFurniture[0])
    val otherFurniture = Furniture(stringResource(R.string.string_other))

    Row(
        Modifier
            .padding(VisioGlobeAppTheme.dims.padding_large)
            .clickable {
                expanded = !expanded
            }) {
        Text(
            text = stringResource(viewModel.mapper.mapFurnitureNameToResId(furnitureSelected.name)),
            fontSize = VisioGlobeAppTheme.dims.text_normal,
            color = MaterialTheme.colors.onBackground
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )

        DropdownMenu(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.8f),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            (listFurniture + otherFurniture).forEach { furniture ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        viewModel.setFurnitureSelected(furniture)
                    }) {
                    val isSelected = furniture.name == furnitureSelected.name
                    val style = if (isSelected) {
                        MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = VisioGlobeAppTheme.dims.text_normal,
                            color = MaterialTheme.colors.primary
                        )
                    } else {
                        MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = VisioGlobeAppTheme.dims.text_normal,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    val predictionAccuracy = stringResource(
                        id = R.string.accuracy,
                        furniture.confidence.percentage()
                    )
                    MenuItem(
                        furnitureMapped = stringResource(
                            id = viewModel.mapper.mapFurnitureNameToResId(
                                furniture.name
                            )
                        ),
                        confidence = predictionAccuracy,
                        style = style
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(furnitureMapped: String, confidence: String, style: TextStyle) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start)
    {
        Text(
            text = furnitureMapped,
            style = style,
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = confidence,
            style = style,
            fontSize = VisioGlobeAppTheme.dims.text_small,
            modifier = Modifier
                .padding(start = VisioGlobeAppTheme.dims.padding_normal)
                .alignByBaseline()
        )
    }
}
