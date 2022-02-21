package com.example.visioglobe.ui.components.incident

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme


@Composable
fun IncidentHeaderBackButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            modifier = Modifier.padding(start = VisioGlobeAppTheme.dims.padding_normal),
            onClick = onClick,
        ) {
            Icon(
                Icons.Default.ArrowBackIosNew,
                modifier = Modifier.size(VisioGlobeAppTheme.dims.back_button_size),
                contentDescription = "",
                tint = MaterialTheme.colors.primary
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5.copy(
                fontSize = VisioGlobeAppTheme.dims.text_large
            )
        )
    }
}

@Composable
fun IncidentDetailsRow(
    content: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colors.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = VisioGlobeAppTheme.dims.padding_small),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            modifier = Modifier
                .padding(end = VisioGlobeAppTheme.dims.padding_small)
                .alignByBaseline(),
            tint = color
        )
        Text(
            text = content,
            modifier = Modifier.alignByBaseline(),
            color = color,
            style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_normal)
        )
    }
}

@Composable
fun IncidentUserProfileContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(
                top = VisioGlobeAppTheme.dims.padding_small,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_small
            ),
        elevation = VisioGlobeAppTheme.dims.padding_extra_small,
        shape = RoundedCornerShape(VisioGlobeAppTheme.dims.padding_small)
    ) {
        Column(Modifier.padding(VisioGlobeAppTheme.dims.padding_normal)) {
            content()
        }
    }
}

@Composable
fun IncidentDescContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(
                top = VisioGlobeAppTheme.dims.padding_normal,
                start = VisioGlobeAppTheme.dims.padding_normal,
                end = VisioGlobeAppTheme.dims.padding_normal,
                bottom = VisioGlobeAppTheme.dims.padding_small
            ),
        elevation = VisioGlobeAppTheme.dims.padding_zero
    ) {
        Column(
            Modifier.padding(VisioGlobeAppTheme.dims.padding_normal)
        ) {
            content()
        }
    }
}

/////////////////////////////////////////////////////////////

@Preview(name = "User Profile Card", showBackground = true)
@Composable
fun ProfileCardPreview() {
    IncidentUserProfileContainer {
        Text("User Profile Preview")
    }
}

@Preview(name = "Simple Card Detail Row", showBackground = true)
@Composable
fun IncidentDetailsRowPreview() {
    IncidentDetailsRow(content = "Armchair", icon = Icons.Default.Handyman)
}


@Preview(name = "Colored Card Detail Row", showBackground = true)
@Composable
fun IncidentDetailsRowColorPreview() {
    VisioGlobeAppTheme() {
        IncidentDetailsRow(
            content = "Date",
            icon = Icons.Default.CalendarViewMonth,
            MaterialTheme.colors.primary
        )
    }
}

@Preview(name = "Long Desc Card Detail Row", showBackground = true)
@Composable
fun IncidentDetailRowLongDescPreview() {
    IncidentDetailsRow(
        content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        icon = Icons.Default.Description
    )
}

