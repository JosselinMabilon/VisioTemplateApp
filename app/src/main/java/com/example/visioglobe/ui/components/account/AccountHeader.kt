package com.example.visioglobe.ui.components.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.visioglobe.ui.theme.VisioGlobeAppTheme
import com.example.visioglobe.R
import com.example.visioglobe.domain.model.User

@Composable
fun AccountHeader(userInfo: User) {
    Surface(color = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier
                .padding(VisioGlobeAppTheme.dims.padding_normal)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .width(VisioGlobeAppTheme.dims.header_width)
                    .height(VisioGlobeAppTheme.dims.header_height)
                    .border(
                        border = BorderStroke(
                            VisioGlobeAppTheme.dims.corner_shape_header,
                            color = MaterialTheme.colors.primary
                        ),
                        shape = CircleShape,
                    )
                    .clip(shape = CircleShape)
                    .padding(
                        top = VisioGlobeAppTheme.dims.padding_normal,
                        start = VisioGlobeAppTheme.dims.padding_small,
                        end = VisioGlobeAppTheme.dims.padding_small,
                        bottom = VisioGlobeAppTheme.dims.padding_normal
                    )
            )
            Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_small))
            Text(
                text = "${userInfo.firstName} ${userInfo.lastName}",
                style = MaterialTheme.typography.h5.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_large
                ),
                color = MaterialTheme.colors.onSecondary
            )
            Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_extra_small))
            Text(
                text = userInfo.email,
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal
                ),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.padding(VisioGlobeAppTheme.dims.padding_extra_small))
            Text(
                text = userInfo.phone,
                style = MaterialTheme.typography.h6.copy(
                    fontSize = VisioGlobeAppTheme.dims.text_normal
                ),
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    VisioGlobeAppTheme {
        AccountHeader(
            User(
                "josselin",
                "josselin@gmail.com",
                "josselin",
                "Test",
                "user",
                "(+33)600000000",
                "XXXXXXXXXXXXXXXXX"
            )
        )
    }
}