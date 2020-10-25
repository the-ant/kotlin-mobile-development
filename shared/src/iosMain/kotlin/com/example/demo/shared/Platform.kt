package com.example.demo.shared


import platform.UIKit.UIDevice
import platform.Foundation.NSUUID

actual class Platform actual constructor() {
    actual val platform: String = "IOS" + UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    actual val uuid: String = "IOS UUID" + " " + NSUUID().UUIDString()
}