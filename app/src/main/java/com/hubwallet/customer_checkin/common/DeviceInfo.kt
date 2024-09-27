package com.hubwallet.customer_checkin.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat

class DeviceInfo {
    fun getDeviceSerial(applicationContext: Context): String? {

        var serialNumber: String?

        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)

            serialNumber = get.invoke(c, "gsm.sn1") as String

            when (serialNumber) {
                "" -> serialNumber = get.invoke(c, "ril.serialnumber") as String
            }

            when (serialNumber) {
                "" -> serialNumber = get.invoke(c, "ro.serialno") as String
            }

            when (serialNumber) {
                "" -> serialNumber = get.invoke(c, "sys.serialnumber") as String
            }

            @Suppress("DEPRECATION")
            when (serialNumber) {
                "" -> serialNumber = Build.SERIAL
            }

            when (serialNumber) {
                "" -> serialNumber = null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            serialNumber = null
        }

        if (serialNumber == "unknown") {
            try {
                val c = Class.forName("android.os.SystemProperties")
                val get = c.getMethod(
                    "get",
                    String::class.java,
                    String::class.java
                )
                serialNumber = get.invoke(c, "ril.serialnumber", "unknown") as String
            } catch (ignored: Exception) {
                Toast.makeText(applicationContext, "ignored ${ignored.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && serialNumber == "unknown") {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                serialNumber = Settings.Secure.getString(
                    applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && serialNumber == "unknown") {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_PRECISE_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                serialNumber = Build.getSerial()
            }
        }
        if(serialNumber==null||serialNumber=="unknown"){
            serialNumber=Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID)
        }
        return serialNumber
    }
}