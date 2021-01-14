package com.tkm.mylibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tkm.library.connection.ConnectionClass
import com.tkm.library.connection.ParameterResult
import com.tkm.library.connection.ResponseConnection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    private var connection: ResponseConnection? = null

    companion object {
        private const val server = "192.168.2.100"
        private const val port = 1433
        private const val database = "OSD_INVC"
        private const val username = "sa"
        private const val password = "12345"
        private const val timeout = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED)
    }

    fun connect(view: View) {
        connection =
            ConnectionClass.openConnection(server, port, database, username, password, timeout)
        if (connection?.isConnection != null) {
            tv_status.text = connection?.isMessage
        } else {
            tv_status.text = connection?.isMessage
        }
    }

    private lateinit var resultData: ResultData

    fun getInformation(view: View) {
        val v = connection?.isConnection
        if (v != null) {
            var statement: Statement? = null
            try {

                //query
                /*statement = connection?.isConnection!!.createStatement()
                val resultSet: ResultSet = statement.executeQuery("SELECT [user_position] FROM [Users] Where [user_id] = '2';")
                while (resultSet.next()) {
                    println("Read: " + resultSet.getString(1))
                    tv_result.text = resultSet.getString(1)
                }*/

                val parameters = ArrayList<ParameterResult>()
                parameters.add(ParameterResult("UserID", "1"))
                parameters.add(ParameterResult("Password", "1"))
                parameters.add(ParameterResult("FromHT", true))

                val ps = ConnectionClass.setConnection(v, "SP_LOGIN_GET_INFORMATION", parameters)


                var isResultSet = ps.execute()
                var count = 0

                while (true) {
                    if (isResultSet) {
                        val rs = ps.resultSet
                        while (rs.next()) {
                            when (count) {
                                0 -> {
                                    resultData = ResultData(
                                        rs.getInt("IsComplete"),
                                        rs.getString("ResponseDescriptionEng"),
                                        rs.getString("ResponseDescriptionThai")
                                    )
                                    println(resultData)
                                }
                                1 -> {
                                    val daoUser = UserData(
                                        true,
                                        rs.getString("UserID"),
                                        rs.getString("Password"),
                                        rs.getString("FirstName"),
                                        rs.getString("LastName"),
                                        rs.getString("Department"),
                                        rs.getString("UserGroup"),
                                        rs.getString("Status"),
                                        rs.getString("CreateUser"),
                                        rs.getString("CreateDate"),
                                        rs.getString("EditUser"),
                                        rs.getString("EditDate")
                                    )
                                    tv_result.text = daoUser.toString()
                                }
                            }
                        }
                        rs.close()
                    } else {
                        if (ps.updateCount == -1) {
                            println("Result $count is just a count: ${ps.updateCount}")
                            break
                        }
                    }
                    count++
                    isResultSet = ps.moreResults
                }



                v.close()
            } catch (e: SQLException) {
                tv_result.text = e.message.toString()
            }
        } else {
            tv_result.text = "Connection is null"
        }
    }

    fun run(view: View) {
        tv_status.text = "Loading..."
        Log.e("Main",  "Name1: ${Thread.currentThread().name}")

        CoroutineScope(Dispatchers.IO).launch {

            val dao = download()

            withContext(Dispatchers.Main){
                tv_status.text = dao.toString()
                Log.e("Main",  "Name3: ${Thread.currentThread().name}")
            }
        }

    }

    private suspend fun download(): UserData? {
        delay(3000)
        connection = ConnectionClass.openConnection(server, port, database, username, password, timeout)

        val v = connection?.isConnection
        if (v != null) {

            val parameters = ArrayList<ParameterResult>()
            parameters.add(ParameterResult("UserID", "1"))
            parameters.add(ParameterResult("Password", "1"))
            parameters.add(ParameterResult("FromHT", true))

            val ps = ConnectionClass.setConnection(v, "SP_LOGIN_GET_INFORMATION", parameters)

            var isResultSet = ps.execute()
            var count = 0

            while (true) {
                if (isResultSet) {
                    val rs = ps.resultSet
                    while (rs.next()) {
                        when (count) {
                            0 -> {
                                resultData = ResultData(
                                    rs.getInt("IsComplete"),
                                    rs.getString("ResponseDescriptionEng"),
                                    rs.getString("ResponseDescriptionThai")
                                )
                                println(resultData)
                            }
                            1 -> {
                                val daoUser = UserData(
                                    true,
                                    rs.getString("UserID"),
                                    rs.getString("Password"),
                                    rs.getString("FirstName"),
                                    rs.getString("LastName"),
                                    rs.getString("Department"),
                                    rs.getString("UserGroup"),
                                    rs.getString("Status"),
                                    rs.getString("CreateUser"),
                                    rs.getString("CreateDate"),
                                    rs.getString("EditUser"),
                                    rs.getString("EditDate")
                                )
                                Log.e("Main",  "Name2: ${Thread.currentThread().name}")
                                return daoUser
                            }
                        }
                    }
                    rs.close()
                } else {
                    if (ps.updateCount == -1) {
                        println("Result $count is just a count: ${ps.updateCount}")
                        break
                    }
                }
                count++
                isResultSet = ps.moreResults
            }

            v.close()
        }

        return null
    }

    fun main() = runBlocking {
        repeat(1_000_000) { counter ->
            launch {
                print(" $counter")
            }
        }
    }


}