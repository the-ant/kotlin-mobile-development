package com.example.demo.shared

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.util.*
import kotlinx.coroutines.*


internal expect val ApplicationDispatcher: CoroutineDispatcher

const val BASE_URL = "http://192.168.24.13:8081/"
const val URL_LOGIN = "v1/users/login"
const val URL_SIGN_UP = "v1/users/register"
const val URL_GET_PROFILE = "v1/users"
class Api {

    private val client = HttpClient(OkHttp) {
        install(DefaultRequest) {
        }
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    //var address = "http://192.168.24.13:8081/v1/users/login"
    @KtorExperimentalAPI
    fun login(
        username: String = "test01",
        password: String = "1",
        callback: (ResponseData<Data>) -> Unit = {}
    ) {
        val url = BASE_URL + URL_LOGIN
        android.util.Log.d("Api", "login: $url")
        CoroutineScope(ApplicationDispatcher).launch {
            try {
                client.post<ResponseData<Data>>(url) {
                    body = MultiPartFormDataContent(
                        formData {
                            append("userName", username)
                            append("password", password)
                        }
                    )
                }.also {
                    Log.d("TAG", "login: $it")
                    if (it.status.value == 200) {
//                        mToken = it.data.token
//                        mLogin.postValue(true)
                        callback(it)
                    }
                }
                client.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @KtorExperimentalAPI
    fun signUp(
        name: String = "test01",
        pass: String = "1",
        display: String = "NONE",
        callback: (ResponseData<Data>) -> Unit = {}
    ) {
        val url = BASE_URL + URL_SIGN_UP
        android.util.Log.d("Api", "login: $url")
        CoroutineScope(ApplicationDispatcher).launch {
            try {
                client.post<ResponseData<Data>>(url) {
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
                        callback(it)
                    }
                }
                client.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @KtorExperimentalAPI
    fun getProfile(
        mToken: String = "",
        callback: (ResponseData<User>) -> Unit = {}
    ) {
        val url = BASE_URL + URL_GET_PROFILE
        android.util.Log.d("Api", "login: $url")
        CoroutineScope(ApplicationDispatcher).launch {
            try {
                val authorization = "Bearer $mToken"
                client.get<ResponseData<User>>(url) {
                    headers {
                        Log.d("TAG", "getProfile: $authorization")
                        append("Accept", "*/*")
                        append("Authorization", authorization)
                    }
                }.also {
                    Log.d("TAG", "profile: $it")
                    callback(it)
                }
                client.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}

