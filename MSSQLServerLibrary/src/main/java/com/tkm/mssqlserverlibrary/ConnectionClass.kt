package com.tkm.mssqlserverlibrary

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionClass {
    companion object {
        private var isSuccess = false
        private var isConnection: Connection? = null
        private var isMessage: String? = null

        fun openConnection(server: String, port: Int, database: String, user: String, password: String, timeout: Int): ResponseConnection {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connectionURL = "jdbc:jtds:sqlserver://$server:$port;databaseName=$database;loginTimeout=$timeout;socketTimeout=$timeout"
                val connection = DriverManager.getConnection(connectionURL, user, password)

                isSuccess = true
                isConnection = connection
                isMessage = "Connected"

            } catch (ex: SQLException) {

                isSuccess = false
                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 1 : ", ex.message.toString())
            } catch (ex: ClassNotFoundException) {

                isSuccess = false
                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 2 : ", ex.message.toString())
            } catch (ex: Exception) {

                isSuccess = false
                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 3 : ", ex.message.toString())
            }
            return ResponseConnection(isSuccess, isConnection, isMessage)
        }

        fun closeConnection(): ResponseConnection {
            try {
                val isOpen = !isConnection?.isClosed!!
                if (isOpen) isConnection?.close()

                isSuccess = true
                isConnection = null
                isMessage = "Disconnected"

            }catch (ex: Exception){

                isSuccess = false
                isConnection = null
                isMessage = ex.message.toString()

                Log.e("error here 4 : ", ex.message.toString())
            }
            return ResponseConnection(isSuccess, isConnection, isMessage)
        }
    }
}