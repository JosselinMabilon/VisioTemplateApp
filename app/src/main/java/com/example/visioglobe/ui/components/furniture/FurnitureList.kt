package com.example.visioglobe.ui.components.furniture

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visioglobe.R
import com.example.visioglobe.domain.mapper.FurnitureNameMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.extension.percentage
import com.example.visioglobe.ui.fragment.CameraFragmentDirections
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

@ExperimentalFoundationApi
@Composable
fun FurnitureList(
    furnitureList: List<Furniture>,
    navController: NavController,
    mapper: FurnitureNameMapper
) {
    Surface(
        modifier = Modifier.background(color = Color.Transparent)
    ) {
        LazyColumn(
            Modifier
                .background(color = Color.Transparent)
                .fillMaxWidth()
                .padding(
                    start = VisioGlobeAppTheme.dims.padding_large,
                    bottom = VisioGlobeAppTheme.dims.padding_large
                ),
            verticalArrangement = Arrangement.spacedBy(VisioGlobeAppTheme.dims.padding_small)
        ) {
            items(
                items = furnitureList,
                itemContent = {
                    FurnitureListItem(
                        mapper = mapper,
                        furniture = it,
                        listFurniture = furnitureList,
                        navController = navController,
                    )
                    Divider()
                }
            )
        }
    }
}

@Composable
fun FurnitureListItem(
    mapper: FurnitureNameMapper,
    furniture: Furniture,
    listFurniture: List<Furniture>,
    navController: NavController
) {
    val resources = LocalContext.current.resources
    val predictionAccuracy = furniture.confidence.percentage()

    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    CameraFragmentDirections.actionCameraFragmentToIncidentDeclarationFragment(
                        listFurniture.toTypedArray(), furniture
                    )
                )
            }
            .background(color = Color.Transparent)) {
        Column(
            modifier = Modifier
                .padding(VisioGlobeAppTheme.dims.padding_small)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(mapper.mapFurnitureNameToResId(furniture.name)),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal
                ),
                color = MaterialTheme.colors.primary,
            )

            Text(
                text = resources.getString(
                    R.string.confidence,
                    predictionAccuracy
                ),
                style = MaterialTheme.typography.caption.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_small
                )
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .offset(x = (-20).dp)
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
