package com.example.visioglobe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    borderWidth: Dp = if (backgroundColor == Color.Transparent) VisioGlobeAppTheme.dims.border_width_large
    else VisioGlobeAppTheme.dims.border_width_large,
    borderColor: Color = MaterialTheme.colors.primary,
    buttonText: String,
    buttonTextColor: Color = if (backgroundColor == Color.Transparent) MaterialTheme.colors.primary
    else Color.White,
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(16),
        border = BorderStroke(
            width = borderWidth,
            color = borderColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = VisioGlobeAppTheme.dims.padding_between_button,
                end = VisioGlobeAppTheme.dims.padding_between_button
            )
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.h6.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            color = buttonTextColor,
            modifier = Modifier.padding(
                top = VisioGlobeAppTheme.dims.padding_extra_small,
                bottom = VisioGlobeAppTheme.dims.padding_extra_small
            )
        )
    }
}

@Preview("Default with Light Theme")
@Composable
fun OutlinedButtonLightThemePreview() {
    VisioGlobeAppTheme(darkTheme = false) {
        Surface {
            CustomOutlinedButton(
                onClick = { },
                buttonText = "Next"
            )
        }
    }
}

@Preview("Transparent with Light Theme")
@Composable
fun OutlinedButtonTransparentLightThemePreview() {
    VisioGlobeAppTheme(darkTheme = false) {
        Surface {
            CustomOutlinedButton(
                onClick = { },
                buttonText = "Next",
                backgroundColor = Color.Transparent
            )
        }
    }
}

@Preview("Default with Dark Theme")
@Composable
fun OutlinedButtonDarkThemePreview() {
    VisioGlobeAppTheme(darkTheme = true) {
        Surface {
            CustomOutlinedButton(
                onClick = { },
                buttonText = "Next"
            )
        }
    }
}

@Preview("Transparent with Dark Theme")
@Composable
fun OutlinedButtonTransparentDarkThemePreview() {
    VisioGlobeAppTheme(darkTheme = true) {
        Surface {
            CustomOutlinedButton(
                onClick = { },
                buttonText = "Next",
                backgroundColor = Color.Transparent
            )
        }
    }
}