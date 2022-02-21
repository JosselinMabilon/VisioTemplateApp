package com.example.visioglobe.extension

import androidx.annotation.VisibleForTesting

@VisibleForTesting
const val PERCENTAGE = 100

fun Float.percentage(): Float {
    return (this * PERCENTAGE)
}
