package com.tkm.library

data class UserData(
    val remember: Boolean,
    val UserID: String,
    val Password: String,
    val FirstName: String,
    val LastName: String,
    val Department: String,
    val UserGroup: String,
    val Status: String,
    val CreateUser: String,
    val CreateDate: String,
    val EditUser: String,
    val EditDate: String
)