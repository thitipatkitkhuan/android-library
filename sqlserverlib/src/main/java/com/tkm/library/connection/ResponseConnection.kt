package com.tkm.library.connection

import java.sql.Connection

data class ResponseConnection(
    val isConnection: Connection? = null,
    val isMessage: String? = null
)