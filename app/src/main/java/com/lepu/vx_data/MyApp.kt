package com.lepu.vx_data

import android.app.Application
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.algorithm.Init
import com.lepu.serial.listener.SerialConnentListener
import com.lepu.serial.manager.SerialPortManager

class MyApp: Application() {

    companion object {
        // 按照我们在Java中一样创建一个单例最简单的方式：
        private var instance:Application?=null
        fun instance()= instance!!
    }

    override fun onCreate() {
        super.onCreate()
        // https://github.com/JeremyLiao/LiveEventBus/blob/master/docs/config.md
        LiveEventBus.config()
            .lifecycleObserverAlwaysActive(true)
            .enableLogger(false)

        instance=this

        Init.init(this)

        SerialPortManager.getInstance()
            .init(this, "/dev/ttyS1", 460800, object : SerialConnentListener {
                override fun onSuccess() {}
                override fun onFail() {}
            })
      //  SerialPortManager.getInstance().setTestMode(true)
      //  EcgDataSaveManager.getInstance().init(this)

    }





}