package com.tkm.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.tkm.mssqlserverlibrary.ConnectionClass
import com.tkm.mssqlserverlibrary.ParameterResult
import com.tkm.mssqlserverlibrary.ResponseConnection
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.SQLException
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    private var connection: ResponseConnection? = null

    companion object {
        private const val server = "192.168.0.181"
        private const val port = 1433
        private const val database = "OSD_INVC"
        private const val username = "sa"
        private const val password = "sato12345"
        private const val timeout = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED)

    }

    fun connect(view: View) {
        connection = ConnectionClass.openConnection(server, port, database, username, password, timeout)
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

                val ps = ConnectionClass.setConnection(v,"SP_LOGIN_GET_INFORMATION", parameters)



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
                                    println(daoUser)
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

    private var dialog: MaterialDialog? = null

    fun showClearDialog() {
        dialog?.dismiss()
        dialog = MaterialDialog(this)
            .title(R.string.delete_all)
            .message(R.string.are_you_sure_delete_all)
            .positiveButton(android.R.string.ok, click = {

            })
            .cornerRadius(16f)
            .negativeButton(android.R.string.cancel)
        dialog?.show()
    }
}