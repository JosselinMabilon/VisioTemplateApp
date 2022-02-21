package com.example.visioglobe.ui.components.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WifiProtectedSetup
import androidx.compose.material.icons.rounded.OndemandVideo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.ui.components.incident.IncidentListItem
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.viewmodel.IncidentsViewModel


@Composable
fun ListUsers(
    users: List<User>,
) {
    val listState = rememberLazyListState()

    LazyColumn(Modifier.padding(VisioGlobeAppTheme.dims.padding_small), state = listState) {
        items(
            users
        ) { user ->
            UserListItem(user, {})
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = VisioGlobeAppTheme.dims.padding_extra_small,
) {
    Surface(
        elevation = elevation,
        shape = RoundedCornerShape(VisioGlobeAppTheme.dims.padding_small),
        modifier = modifier
            .padding(VisioGlobeAppTheme.dims.padding_extra_small)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(VisioGlobeAppTheme.dims.padding_normal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .border(
                        border = BorderStroke(
                            2.dp,
                            color = MaterialTheme.colors.primary
                        ),
                        shape = CircleShape,
                    )
                    .clip(shape = CircleShape)
                    .padding(VisioGlobeAppTheme.dims.padding_small)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = VisioGlobeAppTheme.dims.padding_normal)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = user.firstName,
                        style = MaterialTheme.typography.body1.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                    )

                    Text(
                        text = user.lastName,
                        style = MaterialTheme.typography.body1.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                        modifier = Modifier.padding(start = VisioGlobeAppTheme.dims.padding_small)
                    )
                }
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.body2.copy(fontSize = VisioGlobeAppTheme.dims.text_normal),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.wrapContentWidth(Alignment.Start)
                )

            }
            Text(
                text = user.permission,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.caption.copy(fontSize = VisioGlobeAppTheme.dims.text_small),
                modifier = Modifier
                    .padding(start = VisioGlobeAppTheme.dims.padding_normal)
                    .wrapContentWidth(Alignment.Start)
            )
        }

    }
}

////////////////////////////////

val userTest = User(
    "56vhz5hfjz56",
    "magali.arhainx@orange.com",
    "Magali",
    "Arhainx",
    "admin",
    "0612345678",
    "Gailleton"
)

val userTest2 = User(
    "56vhz5hfjz56",
    "magali.arhainx@orange.com",
    "Josselin",
    "Mabilon",
    "user",
    "0612345678",
    "Gailleton"
)

@Preview("User List Item", showBackground = true, device = Devices.DEFAULT)
@Composable
private fun UserListItemPreview() {
    VisioGlobeAppTheme() {
        ListUsers(listOf(userTest, userTest2))
    }
}