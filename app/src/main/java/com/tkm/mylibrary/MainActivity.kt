package com.tkm.mylibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tkm.library.connection.ConnectionClass
import com.tkm.library.connection.ParameterResult
import com.tkm.library.connection.ResponseConnection
import com.tkm.mylibrary.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var connection: ResponseConnection? = null

    companion object {
        private const val server = "192.168.2.103"
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
    }

}