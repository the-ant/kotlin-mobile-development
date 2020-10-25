package com.example.demo.androidApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.shared.Api
import com.example.demo.shared.Greeting
import io.ktor.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

fun greet(): String {
    return Greeting().greeting()
}

fun randomUUID(): String {
    return Greeting().randomUUID()
}

class MainActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var mToken: String = ""

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("SetTextI18n")
    @KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.tvPlatform)
        tv.text = greet()
        val tvUUID: TextView = findViewById(R.id.tvUUID)
        tvUUID.text = randomUUID()

        val edtUserName: EditText = findViewById(R.id.edtUserName)
        val edtPassword: EditText = findViewById(R.id.edtPassword)
        val edtDisplayName: EditText = findViewById(R.id.edtDisplayName)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnSignUp.setOnClickListener {
            Api().signUp(
                edtUserName.text.toString().trim(),
                edtPassword.text.toString().trim(),
                edtDisplayName.text.toString().trim()
            ) { data ->
                Log.d("TAG", "signUp: ${data.data.token}")
                mToken = data.data.token
            }
        }

        btnLogin.setOnClickListener {
            Api().login(
                edtUserName.text.toString().trim(),
                edtPassword.text.toString().trim()
            ) { data ->
                Log.d("TAG", "login: ${data.data.token}")
                mToken = data.data.token
            }
        }

        btnGetProfile.setOnClickListener {
            Api().getProfile(mToken) { user ->
                Log.d("TAG", "getProfile: ${user.data.displayName}")
            }
        }
    }

}