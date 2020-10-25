package com.example.demo.shared

class Greeting {
    fun greeting(): String {
        return "${Platform().platform}!"
    }

    fun randomUUID(): String {
        return "${Platform().uuid}"
    }
}
