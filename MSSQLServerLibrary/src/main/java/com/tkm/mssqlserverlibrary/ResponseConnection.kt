package com.tkm.mssqlserverlibrary

import java.sql.Connection

data class ResponseConnection(
    val isSuccess: Boolean,
    val isConnection: Connection? = null,
    val isMessage: String? = null
)