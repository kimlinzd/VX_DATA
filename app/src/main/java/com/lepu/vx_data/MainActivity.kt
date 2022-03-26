package com.lepu.vx_data

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.serial.constant.EventMsgConst
import com.lepu.serial.obj.*
import com.lepu.serial.service.SerialService
import com.lepu.vx_data.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import android.content.Intent
import com.lepu.serial.constant.SerialCmd
import com.lepu.serial.manager.SerialPortManager
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var executorSave = Executors.newFixedThreadPool(1) as ThreadPoolExecutor
    private lateinit var file:File

//    lateinit var serialService: SerialService
//    private val serialConn = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            LogUtils.d("serialConn connected")
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            LogUtils.d("serialConn disconnected")
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   SerialPortManager.getInstance().setTestMode(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        file= FileUtils.createFile(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.txt")

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

       // initService()

        observeLiveDataObserve()

//        var array = EcgDemoWave.Main()
//        FilterManager.getInstance().filter(null,array,7,null)
    }

    var ecgIndex=0;
    var respIndex=0;

    /**
     * 在这里初始化服务
     */
    private fun initService() {
         SerialService.startService(this)
     }


    private fun observeLiveDataObserve() {
        thread {
            while (true){
                sleep(100)
                SerialPortManager.getInstance()
                    .serialSendData(
                        SerialCmd.cmdNibpReadBpModuleInfo()
                    )
            }

        }



        //接收到心电图信息
        LiveEventBus.get(EventMsgConst.MsgEcgData).observe(this, {
            val data = it as EcgData
            ecgIndex++;

            if(ecgIndex<=125){
           //     executorSave.execute(SaveTask(data.originalData))
            }
               Log.e("接收到心电图信息", data.hr.toString())
        }
        )
        //接收到呼吸数据信息
        LiveEventBus.get(EventMsgConst.MsgRespData).observe(this, {
            val data = it as RespData
            respIndex++


                Log.e("接收到呼吸数据信息", "RR=" + data.rr)
        }
        )

        //接收到呼吸数据信息
        LiveEventBus.get(EventMsgConst.MsgTempData).observe(this, {
            val data = it as TempData
         //   Log.e("接收到体温数据信息", "temp·············· =" + data.temp1+"---"+data.temp2)
        }
        )
        //接收到心电图信息
        LiveEventBus.get(EventMsgConst.MsgSpO2Data).observe(this, {
            val data = it as SpO2Data
    //           Log.e("接收到血氧信息", "PI===="+data.pi)
        }
        )


        //实时袖带压（5Hz）
        LiveEventBus.get(EventMsgConst.MsgNibpCP5HZData).observe(this, {
            val data = it as NibpCP5HZData
       //     Log.e("接收到血压数据信息", "血压数据")
        }
        )
        //血压NIBP 实时袖带压（200Hz）
        LiveEventBus.get(EventMsgConst.MsgNibpCP200HZData).observe(this, {
            val data = it as NibpCP200HZData
     //       Log.e("接收到血压数据信息", "实时原始数据（200Hz）")
        }
        )
        //血压模块工作状态
        LiveEventBus.get(EventMsgConst.NibpWorkingStatus).observe(this, {
            val data = it as NibpWorkingStatus

            Log.e("接收到血压模块工作状态", "血压模块工作状态  +SP=="+data.sp+"--"+data.nibpMsmEnum.value)
        }
        )

        // 血压模块信息
        LiveEventBus.get(EventMsgConst.NibpModuleInfo).observe(this, {
            val data = it as NibpModuleInfo

            Log.e("血压模块信息", "血压模块信息  +mainMCU=="+data.mainMCU)
        }
        )

        //血压参数和模块状态
         LiveEventBus.get(EventMsgConst.MsgSpO2WaveData).observe(this, {
            val data = it as SpO2WaveData

       //   Log.e("血氧波形数据", "data[0]="+data.wave[0]+"-----data[1]="+data.wave[1])
        }
        )
        //血氧波形数据源
        LiveEventBus.get(EventMsgConst.NibpPramAndStatus).observe(this, {
            val data = it as NibpPramAndStatus
         Log.e("测量完毕", "收缩压:" + data.sys + "---" + "舒张压:" + data.dia + "---PR:" + data.pr)
        }
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
           System.exit(0)
        }
        return super.onKeyDown(keyCode, event)
    }
}