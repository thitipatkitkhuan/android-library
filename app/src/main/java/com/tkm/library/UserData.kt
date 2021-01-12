package com.tkm.library

data class UserData(
    val remember: Boolean,
    val UserID: String,
    val Password: String,
    val Name: String,
    val Lastname: String,
    val Department: String,
    val UserGroup: String,
    val Status: String,
    val CreateUser: String,
    val CreateDate: String,
    val EditUser: String,
    val EditDate: String
)