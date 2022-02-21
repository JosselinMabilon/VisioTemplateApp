package com.example.visioglobe.ui.components.incident

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.visioglobe.R
import com.example.visioglobe.ui.components.ShowImageError
import com.example.visioglobe.ui.components.ShowLoading
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

private const val CROSS_FADE_DURATION = 750

@ExperimentalCoilApi
@Composable
fun BoxIncidentImage(imagePath: String?, heightFraction: Double, scale: ContentScale) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (!imagePath.isNullOrEmpty()) {
            val coilPainter = rememberImagePainter(
                data = imagePath,
                builder = {
                    crossfade(CROSS_FADE_DURATION)
                    transformations(
                        RoundedCornersTransformation(VisioGlobeAppTheme.dims.rounded_corners_transformation_radius)
                    )
                },
            )
            when (coilPainter.state) {
                is ImagePainter.State.Error -> ShowImageError()
                is ImagePainter.State.Loading -> ShowLoading()
            }
            Image(
                painter = coilPainter,
                contentScale = scale,
                modifier = Modifier
                    .height((heightFraction * LocalConfiguration.current.screenHeightDp).dp)
                    .fillMaxWidth()
                    .padding(VisioGlobeAppTheme.dims.padding_small),
                contentDescription = stringResource(R.string.incident_picture_desc),
            )
        } else {
            ShowImageError()
        }
    }
}
