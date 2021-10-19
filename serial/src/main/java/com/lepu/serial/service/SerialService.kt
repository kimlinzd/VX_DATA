package com.lepu.serial.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class SerialService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    private val binder = BleBinder()

    override fun onBind(p0: Intent): IBinder {
        return binder
    }



    inner class BleBinder: Binder() {
        fun getService(): SerialService = this@SerialService
    }

    companion object {
        @JvmStatic
        fun startService(context: Context) {
            Intent(context, SerialService::class.java).also { intent -> context.startService(intent)
            }
        }

        @JvmStatic
        fun stopService(context: Context) {
            val intent = Intent(context, SerialService::class.java)
            context.stopService(intent)
        }

    }
}