package com.example.visioglobe.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Incident(
    var reporterName: String,
    var reporterFirstname: String,
    var email: String?,
    var phoneNumber: String?,
    var site: String,
    var location: String,
    var time: Long,
    var title: String,
    var description: String?,
    var furniture: Furniture,
    var imageId: String? = null
) : Parcelable
