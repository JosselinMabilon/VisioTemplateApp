package com.example.visioglobe.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Furniture(
    var name: String,
    val confidence: Float,
    val index: Int,
    val imageUri: Uri?
) : Parcelable {
    constructor(name: String) : this(name, 0f, -1, null)

    override fun toString(): String {
        return "$name : $confidence (index $index)"
    }
}