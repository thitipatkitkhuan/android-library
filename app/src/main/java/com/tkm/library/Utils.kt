package com.tkm.library

import java.lang.StringBuilder
import java.sql.Connection
import java.sql.PreparedStatement

class Utils {
    companion object{

        fun setConnection(connection: Connection, column: String, parameters: ArrayList<ParameterResult>): PreparedStatement {

            val sql = StringBuilder()
            sql.append("EXEC $column")

            parameters.forEachIndexed { index, element ->
                sql.append(" @${element.column} = ?")
                if(index != (parameters.size - 1)){
                    sql.append(",")
                }
            }

            val ps = connection.prepareStatement(sql.toString())

            ps.setEscapeProcessing(true)
            ps.queryTimeout = 10

            parameters.forEachIndexed { index, element ->
                var i = index + 1
                when (val value = element.value) {
                    is String -> ps.setString(i, value.toString())
                    is Int -> ps.setInt(i, value.toInt())
                    is Double -> ps.setDouble(i, value.toDouble())
                    is Boolean -> ps.setBoolean(i, value)
                }
            }

            return ps
        }
    }
}