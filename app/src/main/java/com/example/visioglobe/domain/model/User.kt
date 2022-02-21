package com.example.visioglobe.domain.model

data class User(
    var id: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var permission: String,
    var phone: String,
    var site: String
)
