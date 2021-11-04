package com.lepu.serial.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.os.bundleOf
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
    var mDevicePath: String = "";
    var mBaudrate: Int = 0;
    override fun onCreate() {
        super.onCreate()
        LogUtils.d("SerialService onCreate")
        //   runTask()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mDevicePath= intent?.getStringExtra(DEVICE_PATH).toString()
        mBaudrate= intent?.getIntExtra(BAUD_RATE,460800)!!

        SerialPortManager.getInstance().init(mDevicePath,mBaudrate)
        EcgDataSaveManager.getInstance().init()

        return super.onStartCommand(intent, flags, startId)
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
                EcgDemoWave.I.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.II.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.III.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.aVR.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.aVL.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.aVF.copyOfRange(index, index + 50).toShortArray(),
                EcgDemoWave.V.copyOfRange(index, index + 50).toShortArray()
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


    inner class BleBinder : Binder() {
        fun getService(): SerialService = this@SerialService
    }

    companion object {
        /**
         * 串口号
         */
       private const val DEVICE_PATH = "devicePath"

        /**
         * 波特率
         */
        private const val BAUD_RATE = "baudrate"

        @JvmStatic
        fun startService(context: Context, devicePath: String, baudrate: Int) {
            Intent(context, SerialService::class.java).also { intent ->
                intent.putExtra(SerialService.DEVICE_PATH, devicePath);
                intent.putExtra(SerialService.BAUD_RATE, baudrate);
                context.startService(intent)
            }
        }

        @JvmStatic
        fun stopService(context: Context) {
            val intent = Intent(context, SerialService::class.java)
            SerialPortManager.getInstance().closeSerialPort();
            EcgDataSaveManager.getInstance().stopTask()
            context.stopService(intent)
        }



    }
}