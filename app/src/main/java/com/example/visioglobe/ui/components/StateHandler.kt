package com.example.visioglobe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

@Composable
fun ShowLoading() {
    CircularProgressIndicator(modifier = Modifier.size(VisioGlobeAppTheme.dims.loading_spinner_size))
}

@Composable
fun ShowError(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_normal),
        style = MaterialTheme.typography.h6.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ShowImageError() {
    Box(contentAlignment = Alignment.Center) {
        Image(
            imageVector = Icons.Default.NoPhotography,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
            modifier = Modifier
                .size(VisioGlobeAppTheme.dims.furniture_image_size)
                .padding(VisioGlobeAppTheme.dims.padding_small),
            contentDescription = stringResource(R.string.incident_picture_desc)
        )
    }
}


////////////////////////////////////////////////////////

@Preview(name = "Error Preview", showBackground = true)
@Composable
fun ShowErrorPreview() {
    ShowError("This is an Error.")
}

@Preview(name = "Loading Preview", showBackground = true)
@Composable
fun ShowLoadingPreview() {
    ShowLoading()
}

@Preview(name = "Image Error Preview", showBackground = true)
@Composable
fun ShowImageErrorPreview() {
    ShowImageError()
}