package com.tkm.mssqlserverlibrary

import android.os.StrictMode
import android.util.Log
import java.lang.StringBuilder
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class ConnectionClass {
    companion object {
        private var isConnection: Connection? = null
        private var isMessage: String? = null

        fun openConnection(server: String, port: Int, database: String, user: String, password: String, timeout: Int): ResponseConnection {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connectionURL = "jdbc:jtds:sqlserver://$server:$port;databaseName=$database;loginTimeout=$timeout;socketTimeout=$timeout"
                val connection = DriverManager.getConnection(connectionURL, user, password)

                isConnection = connection
                isMessage = "Connected"

            } catch (ex: SQLException) {

                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 1 : ", ex.message.toString())
            } catch (ex: ClassNotFoundException) {

                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 2 : ", ex.message.toString())
            } catch (ex: Exception) {

                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 3 : ", ex.message.toString())
            }
            return ResponseConnection(isConnection, isMessage)
        }

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
                val i = index + 1
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