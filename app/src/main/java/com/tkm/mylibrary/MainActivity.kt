package com.tkm.mylibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tkm.library.connection.ConnectionClass
import com.tkm.mylibrary.databinding.ActivityMainBinding
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val server = "192.168.0.148"
        private const val port = 1433
        private const val database = "TestDB"
        private const val username = "sa"
        private const val password = "12345"
        private const val timeout = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOneValue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_one_value -> {
                var connection: Connection? = null
                var resultSet: ResultSet? = null
                try {
                    connection = ConnectionClass.openConnection(
                        server,
                        port.toString(),
                        database,
                        username,
                        password,
                        timeout.toString()
                    )
                    val ps = ConnectionClass.setConnection(connection!!, "SP_GET_ONE_VALUE", null)
                    val isResultSet = ps.execute()
                    if (isResultSet) {
                        resultSet = ps.resultSet
                        if (resultSet.next()) {
                            val status = resultSet.getString("Status")
                            if (status.isNotEmpty()) {
                                setTextResult(status)
                                Log.d("btn_one_value", status)
                                return
                            }
                        }
                    }
                    setTextResult("Not found status")
                    Log.e("btn_one_value", "Not found status")
                } catch (e: SQLException) {
                    setTextResult(e.message.toString())
                    Log.e("btn_one_value", e.message.toString())
                } catch (e: Exception) {
                    setTextResult(e.message.toString())
                    Log.e("btn_one_value", e.message.toString())
                } finally {
                    connection?.close()
                }
            }
        }
    }

    private fun setTextResult(message: String) {
        binding.tvResult.text = ""
        binding.tvResult.text = message
    }
}
