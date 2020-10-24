package com.example.demo.androidApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.demo.shared.Greeting
import com.google.gson.annotations.SerializedName
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun greet(): String {
    return Greeting().greeting()
}

fun randomUUID(): String {
    return Greeting().randomUUID()
}

fun printLog(): String {
    return Greeting().printLog()
}

const val BASE_URL = "http://192.168.1.25:8081/"
const val URL_LOGIN = "v1/users/login"
const val URL_SIGN_UP = "v1/users/register"
const val URL_GET_PROFILE = "v1/users"

class MainActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var mToken: String = ""
    private val mUser = MutableLiveData<ResponseData.User?>()
    private val mLogin = MutableLiveData<Boolean>()
    private val mSignUp = MutableLiveData<Boolean>()

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
        tvUUID.text = randomUUID() + " " + printLog()

        val edtUserName: EditText = findViewById(R.id.edtUserName)
        val edtPassword: EditText = findViewById(R.id.edtPassword)
        val edtDisplayName: EditText = findViewById(R.id.edtDisplayName)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnSignUp.setOnClickListener {
            signUp(
                edtUserName.text.toString().trim(),
                edtPassword.text.toString().trim(),
                edtDisplayName.text.toString().trim()
            )
        }

        btnLogin.setOnClickListener {
            login(
                edtUserName.text.toString().trim(),
                edtPassword.text.toString().trim()
            )
        }

        btnGetProfile.setOnClickListener {
            getProfile()
        }

        mUser.observe(this) {
            it?.let {
                Toast.makeText(this, it.displayName, Toast.LENGTH_SHORT).show()
            }
        }

        mSignUp.observe(this) {
            if (it) {
                Toast.makeText(this, "SignUp success", Toast.LENGTH_SHORT).show()
            }
        }

        mLogin.observe(this) {
            if (it) {
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @KtorExperimentalAPI
    private fun signUp(
        name: String = "",
        pass: String = "",
        display: String = ""
    ) = CoroutineScope(Dispatchers.IO).launch {
        val url = BASE_URL + URL_SIGN_UP
        val client = HttpClient(OkHttp) {
            install(DefaultRequest) {
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
        try {
            client.post<ResponseData<ResponseData.Data>>(url) {
                body = MultiPartFormDataContent(
                    formData {
                        append("userName", name)
                        append("password", pass)
                        append("displayName", display)
                    }
                )
            }.also {
                Log.d("TAG", "sign_up: $it")
                if (it.status.value == 200) {
                    mToken = it.data.token
                    mSignUp.postValue(true)
                }
            }
            client.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun login(
        name: String = "",
        pass: String = ""
    ) = CoroutineScope(Dispatchers.IO).launch {
        val url = BASE_URL + URL_LOGIN
        val client = HttpClient(OkHttp) {
            install(DefaultRequest) {
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
        try {
            client.post<ResponseData<ResponseData.Data>>(url) {
                body = MultiPartFormDataContent(
                    formData {
                        append("userName", name)
                        append("password", pass)
                    }
                )
            }.also {
                Log.d("TAG", "login: $it")
                if (it.status.value == 200) {
                    mToken = it.data.token
                    mLogin.postValue(true)
                }
            }
            client.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun getProfile() = CoroutineScope(Dispatchers.IO).launch {
        val url = BASE_URL + URL_GET_PROFILE
        val client = HttpClient(OkHttp) {
            install(DefaultRequest) {
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
        try {
            val authorization = "Bearer $mToken"
            client.get<ResponseData<ResponseData.User>>(url) {
                headers {
                    Log.d("TAG", "getProfile: $authorization")
                    append("Accept", "*/*")
                    append("Authorization", authorization)
                }
            }.also {
                Log.d("TAG", "profile: $it")
                mUser.postValue(it.data)
            }
            client.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    data class ResponseData<T>(
        @SerializedName("status") val status: Status,
        @SerializedName("message") val message: String,
        @SerializedName("errors") val errors: List<Error>,
        @SerializedName("data") val data: T
    ) {
        data class Status(
            @SerializedName("value") val value: Int,
            @SerializedName("description") val description: String
        )

        data class Error(
            @SerializedName("code") val code: Int,
            @SerializedName("cause") val cause: String
        )

        data class Data(
            @SerializedName("token") val token: String,
            @SerializedName("user") val user: User
        )

        data class User(
            @SerializedName("id") val id: String,
            @SerializedName("userName") val user: String,
            @SerializedName("displayName") val displayName: String
        )
    }
}