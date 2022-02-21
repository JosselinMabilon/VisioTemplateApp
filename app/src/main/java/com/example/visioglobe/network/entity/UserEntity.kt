package com.example.visioglobe.network.entity

import com.google.firebase.firestore.DocumentId

data class UserEntity(
    @DocumentId  val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val permission: String = "",
    val phone: String = "",
    val site: String = ""
)