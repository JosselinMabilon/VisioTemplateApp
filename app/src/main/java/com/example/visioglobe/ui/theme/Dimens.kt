package com.example.visioglobe.ui.theme

import androidx.annotation.Dimension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Dimension(unit = Dimension.DP)
private const val SMALL_DEVICE_SCREEN_WIDTH_DP = 361

@Dimension(unit = Dimension.DP)
private const val REGULAR_DEVICE_SCREEN_WIDTH_DP = 430

@Immutable
data class AppDims(
    val padding_zero: Dp,
    val padding_extra_small: Dp,
    val padding_small: Dp,
    val padding_normal: Dp,
    val padding_large: Dp,
    val padding_extra_large: Dp,
    val padding_log_out: Dp,
    val corner_shape_button: Dp,
    val corner_shape_header: Dp,
    val text_field_desc_incident_height: Dp,
    val header_width: Dp,
    val header_height: Dp,
    val padding_between_button: Dp,
    val obs_logo_width: Dp,
    val obs_logo_height: Dp,
    val no_border_width: Dp,
    val border_width_small: Dp,
    val border_width_normal: Dp,
    val border_width_large: Dp,
    val loading_spinner_size: Dp,
    val furniture_image_size: Dp,
    val rounded_corners_transformation_radius: Float,
    val back_button_size: Dp,
    val information_button: Dp,

    val text_small: TextUnit,
    val text_normal: TextUnit,
    val text_large: TextUnit,

    // Universal dim for all screen size
    val incident_list_item_max_width_fraction: Float = 0.9f,
    val incident_image_detail_height_fraction: Double = 0.3,
    val incident_image_gallery_height_fraction: Double = 0.45,
)

@Composable
fun appDims() = when {
    LocalConfiguration.current.screenWidthDp < SMALL_DEVICE_SCREEN_WIDTH_DP -> {
        smallDeviceAppDims
    }
    LocalConfiguration.current.screenWidthDp in SMALL_DEVICE_SCREEN_WIDTH_DP..REGULAR_DEVICE_SCREEN_WIDTH_DP -> {
        regularAppDims
    }
    else -> {
        largeAppDims
    }
}

fun defaultAppDims() = regularAppDims

private val smallDeviceAppDims = AppDims(
    padding_zero = 0.dp,
    padding_extra_small = 2.dp,
    padding_small = 8.dp,
    padding_normal = 12.dp,
    padding_large = 16.dp,
    padding_log_out = 70.dp,
    padding_extra_large = 24.dp,
    corner_shape_button = 40.dp,
    corner_shape_header = 3.dp,
    text_field_desc_incident_height = 80.dp,
    header_width = 70.dp,
    header_height = 70.dp,
    padding_between_button = 80.dp,
    obs_logo_width = 130.dp,
    obs_logo_height = 90.dp,
    no_border_width = 0.dp,
    border_width_small = (0.5).dp,
    border_width_normal = 1.dp,
    border_width_large = 2.dp,
    text_small = 8.sp,
    text_normal = 12.sp,
    text_large = 16.sp,
    loading_spinner_size = 40.dp,
    furniture_image_size = 150.dp,
    rounded_corners_transformation_radius = 25f,
    back_button_size = 30.dp,
    information_button = 30.dp
)

private val regularAppDims = AppDims(
    padding_zero = 0.dp,
    padding_extra_small = 4.dp,
    padding_small = 8.dp,
    padding_normal = 16.dp,
    padding_large = 24.dp,
    padding_extra_large = 32.dp,
    padding_log_out = 70.dp,
    corner_shape_button = 40.dp,
    corner_shape_header = 3.dp,
    text_field_desc_incident_height = 80.dp,
    header_width = 70.dp,
    header_height = 70.dp,
    padding_between_button = 80.dp,
    obs_logo_width = 150.dp,
    obs_logo_height = 100.dp,
    no_border_width = 0.dp,
    border_width_small = (0.5).dp,
    border_width_normal = 1.dp,
    border_width_large = 2.dp,
    text_small = 12.sp,
    text_normal = 16.sp,
    text_large = 20.sp,
    loading_spinner_size = 50.dp,
    furniture_image_size = 200.dp,
    rounded_corners_transformation_radius = 25f,
    back_button_size = 35.dp,
    information_button = 30.dp
)

private val largeAppDims = AppDims(
    padding_zero = 0.dp,
    padding_extra_small = 16.dp,
    padding_small = 24.dp,
    padding_normal = 32.dp,
    padding_large = 56.dp,
    padding_extra_large = 72.dp,
    padding_log_out = 200.dp,
    corner_shape_button = 40.dp,
    corner_shape_header = 3.dp,
    text_field_desc_incident_height = 140.dp,
    header_width = 180.dp,
    header_height = 180.dp,
    padding_between_button = 160.dp,
    obs_logo_width = 200.dp,
    obs_logo_height = 150.dp,
    no_border_width = 0.dp,
    border_width_small = (0.5).dp,
    border_width_normal = 1.dp,
    border_width_large = 2.dp,
    text_small = 16.sp,
    text_normal = 25.sp,
    text_large = 32.sp,
    loading_spinner_size = 60.dp,
    furniture_image_size = 250.dp,
    rounded_corners_transformation_radius = 25f,
    back_button_size = 35.dp,
    information_button = 30.dp
)