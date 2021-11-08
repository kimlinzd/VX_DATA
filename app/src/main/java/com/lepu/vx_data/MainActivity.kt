package com.lepu.vx_data

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.blankj.utilcode.util.LogUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.algorithm.ecg.manager.FilterManager
import com.lepu.serial.constant.EventMsgConst
import com.lepu.serial.obj.EcgData
import com.lepu.serial.obj.EcgData1
import com.lepu.serial.obj.EcgDemoWave
import com.lepu.serial.service.SerialService
import com.lepu.vx_data.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    /**
     * 在这里初始化服务
     */
    private fun initService() {
         SerialService.startService(this)
     }

    private fun observeLiveDataObserve() {
        LiveEventBus.get(EventMsgConst.MsgEcgData1).observe(this, {
                val data = it as EcgData1
//                LogUtils.d("接收到心电图信息",data.chn.toString())
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
}