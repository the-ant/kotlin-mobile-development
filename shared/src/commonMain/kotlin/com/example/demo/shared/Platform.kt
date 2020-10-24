package com.example.demo.shared

expect class Platform() {
    val platform: String
    val uuid: String
    val printLog: String
}