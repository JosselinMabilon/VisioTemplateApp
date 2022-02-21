package com.example.visioglobe.network.entity

import com.google.firebase.Timestamp

data class IncidentEntity(
    val date: Timestamp = Timestamp.now(),
    val description: String? = "",
    val email: String? = "",
    val firstName: String = "",
    val incidentType: String = "",
    val lastName: String = "",
    val location: String = "",
    val phoneNumber: String? = "",
    val siteId: String = "",
    val siteName: String = "",
    val title: String = "",
    val imageId: String? = ""
)
