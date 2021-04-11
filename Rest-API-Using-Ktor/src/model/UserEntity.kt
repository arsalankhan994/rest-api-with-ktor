package com.erselankhan.model

import kotlinx.serialization.Serializable

@Serializable
class UserEntity(
    val id: String,
    val firstName: String, val lastName: String,
    val phoneNumber: String, val password: String,
    val confirmPassword: String, val emailAddress: String
) {
}