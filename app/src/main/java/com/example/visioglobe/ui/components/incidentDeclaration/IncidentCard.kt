package com.example.visioglobe.ui.components.incidentDeclaration


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import coil.transform.RoundedCornersTransformation
import com.example.visioglobe.R
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme

private const val REQUIRED_SYMBOL = '*'

@Composable
fun IncidentTextTitle(title: String) {
    Text(
        modifier = Modifier
            .padding(
                start = VisioGlobeAppTheme.dims.padding_normal,
                top = VisioGlobeAppTheme.dims.padding_small,
                bottom = VisioGlobeAppTheme.dims.padding_small
            ),
        text = title,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h5.copy(
            fontSize = VisioGlobeAppTheme.dims.text_large
        ),
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun IncidentHeaderRow(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            modifier = Modifier
                .padding(end = VisioGlobeAppTheme.dims.padding_small)
                .alignByBaseline(),
            tint = MaterialTheme.colors.onSurface
        )
        Text(
            text = title,
            style = MaterialTheme.typography.button.copy(
                fontSize = VisioGlobeAppTheme.dims.text_normal
            ),
            modifier = Modifier.alignByBaseline(),
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun IncidentTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    maxLines: Int? = null,
    required: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    TextField(
        shape = RoundedCornerShape(VisioGlobeAppTheme.dims.corner_shape_button),
        placeholder = {
            Text(
                text = if (required) {
                    "$placeholder$REQUIRED_SYMBOL"
                } else {
                    "$placeholder ${stringResource(id = R.string.incident_declaration_field_optional)}"
                },
                style = MaterialTheme.typography.h6.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            )
        },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(
                top = VisioGlobeAppTheme.dims.padding_small,
                end = VisioGlobeAppTheme.dims.padding_small
            ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = maxLines ?: 1,
        singleLine = maxLines?.let { false } ?: true,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }),
        keyboardOptions = keyboardOptions.copy(imeAction = ImeAction.Done),
        textStyle = MaterialTheme.typography.h6.copy(
            fontSize = VisioGlobeAppTheme.dims.text_normal,
            color = MaterialTheme.colors.onPrimary
        )
    )
}

@Composable
fun IncidentSectionContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(VisioGlobeAppTheme.dims.padding_small)
    ) {
        content()
    }
}

@Composable
fun InformationContent(alpha: Float, message: String) {
    Box(
        Modifier
            .background(
                MaterialTheme.colors.background.copy(alpha = alpha),
                RoundedCornerShape(VisioGlobeAppTheme.dims.corner_shape_button)
            )
            .padding(VisioGlobeAppTheme.dims.padding_large)
    ) {
        Text(
            message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSecondary
        )
    }
}