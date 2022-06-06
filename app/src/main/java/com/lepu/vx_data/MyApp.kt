package com.lepu.vx_data

import android.app.Application
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.serial.listener.SerialConnectListener
import androidx.appcompat.app.AppCompatDelegate
import com.lepu.serial.manager.ServeComManager


class MyApp: Application() {

    companion object {
        // 按照我们在Java中一样创建一个单例最简单的方式：
        private var instance:Application?=null
        fun instance()= instance!!
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        // https://github.com/JeremyLiao/LiveEventBus/blob/master/docs/config.md
        LiveEventBus.config()
            .lifecycleObserverAlwaysActive(true)
            .enableLogger(false)

        instance=this



        ServeComManager.getInstance()
            .init(this,  object :
                SerialConnectListener {
                override fun onSuccess() {
                    Log.e("lzd","打开串口成功")
                }
                override fun onFail() {
                    Log.e("lzd","打开串口失败")
                }
            })

      //  SerialPortManager.getInstance().setTestMode(true)
      //  EcgDataSaveManager.getInstance().init(this)

    }





}