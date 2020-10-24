package com.example.demo.shared

import java.util.*

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val uuid: String = "Android UUID: ${UUID.randomUUID().toString()}"
    actual val printLog: String = "Android Log ${println("ABC")}"
}