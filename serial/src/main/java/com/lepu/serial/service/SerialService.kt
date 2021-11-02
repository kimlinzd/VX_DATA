package com.lepu.serial.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.serial.obj.EcgData
import com.lepu.serial.obj.EcgDemoWave
import com.lepu.serial.constant.EventMsgConst
import com.lepu.serial.manager.EcgDataSaveManager
import com.lepu.serial.manager.SerialPortManager
import java.util.*
import kotlin.concurrent.schedule

class SerialService : Service() {

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("SerialService onCreate")
        SerialPortManager.getInstance().init()
        EcgDataSaveManager.getInstance().init()
        runTask()
    }

    var index = 0
    private fun runTask() {
        Timer().schedule(100) {
            runTask()

            /**
             * 获取demo数据，100ms发送一次
             */
            val ecg = EcgData(
                50,
                EcgDemoWave.I.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.II.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.III.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.aVR.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.aVL.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.aVF.copyOfRange(index, index+50).toShortArray(),
                EcgDemoWave.V.copyOfRange(index, index+50).toShortArray()
            )

            LiveEventBus.get(EventMsgConst.MsgEcgData)
                .post(ecg)
            index = (index + 50) % 500
        }
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