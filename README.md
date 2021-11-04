# VX_DATA
AiView android端使用的接口工具 包括打开串口，处理串口数据，向串口写入命令。

add it in your root build.gradle

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
add the dependency

 dependencies {
	        implementation 'com.github.kimlinzd:VX_DATA:1.0.3'
	}
        

*开启服务：
 开启服务会打开串口，并开始读取串口数据
 activity?.let { it1 -> SerialService.startService(it1,"/dev/ttyS1",460800)}

*停止服务：
 停止服务会关闭串口，并停止读取数据的定时任务
 activity?.let { it1 -> SerialService.stopService(it1) }

*设置测试模式：
 SerialPortManager.getInstance().setTestMode(true)
 
 *****现在测试数据只有心电图数据*****

*接收串口数据使用LiveEventBus：
 接收心电数据例子：
 LiveEventBus.get(EventMsgConst.MsgEcgData1).observe(this, {
                val data = it as EcgData1
                LogUtils.d("接收到心电图信息",data.chn.toString())
            }
        )
 
 其他数据：
 血压NIBP：NibpData
 血压NIBP（原始数据）:NibpOriginalData
 呼吸数据:RespData
 血氧SpO2数据:SpO2Data
 血氧SpO2数据(原始数据)：SpO2OriginalData
 体温数据：TempData
 
 LiveEventBus key详见：EventMsgConst
 
 *向串口写入数据（命令）
  数据开始传输命令：
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStart(),cmdReplyListener)
  数据停止传输命令：
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStop(),cmdReplyListener)
  其他命令待完善
        
        

      
