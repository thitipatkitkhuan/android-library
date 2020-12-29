package com.tkm.library

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.tkm.mssqlserverlibrary.ConnectionClass
import com.tkm.mssqlserverlibrary.ResponseConnection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    private lateinit var connection: ResponseConnection

    companion object {
        private const val server = "192.168.0.148"
        private const val port = 1433
        private const val database = "WebAPI"
        private const val username = "sa"
        private const val password = "12345"
        private const val timeout = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.INTERNET),PackageManager.PERMISSION_GRANTED)

        connection = ConnectionClass.openConnection(server, port, database, username, password, timeout)

        if (connection.isSuccess) {
            var statement: Statement? = null
            try {
                statement = connection.isConnection!!.createStatement()
                val resultSet: ResultSet = statement.executeQuery("SELECT [user_position] FROM [Users] Where [user_id] = '2';")
                while (resultSet.next()) {
                    println("Read: " + resultSet.getString(1))
                }
                //ConnectionClass.closeConnection()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } else {
            println("Connection is null")
        }

    }

    fun disConnect(view: View) {
        ConnectionClass.closeConnection() 
    }
}