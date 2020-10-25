package com.example.demo.shared

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(
    @SerializedName("status") val status: Status,
    @SerializedName("message") val message: String,
    @SerializedName("errors") val errors: List<Error>,
    @SerializedName("data") val data: T
)

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
