package com.tkm.library

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.tkm.mssqlserverlibrary.ConnectionClass
import com.tkm.mssqlserverlibrary.ResponseConnection
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    private var connection: ResponseConnection? = null

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

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED)

    }

    fun connect(view: View) {
        connection = ConnectionClass.openConnection(server, port, database, username, password, timeout)
        if (connection?.isConnection != null) {
            tv_status.text = connection?.isMessage
        } else {
            tv_status.text = connection?.isMessage
        }
    }

    fun getInformation(view: View) {
        val v = connection?.isConnection
        if (v != null) {
            var statement: Statement? = null
            try {
                statement = connection?.isConnection!!.createStatement()
                val resultSet: ResultSet = statement.executeQuery("SELECT [user_position] FROM [Users] Where [user_id] = '2';")
                while (resultSet.next()) {
                    println("Read: " + resultSet.getString(1))
                    tv_result.text = resultSet.getString(1)
                }
                v.close()
            } catch (e: SQLException) {
                tv_result.text = e.message.toString()
            }
        } else {
            tv_result.text = "Connection is null"
        }
    }
}