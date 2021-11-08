# VX_DATA
AiView android端使用的接口工具 包括打开串口，处理串口数据，向串口写入命令。

add it in your root build.gradle

allprojects {<br>
		repositories {<br>
			...<br>
			maven { url 'https://jitpack.io' }<br>
		}<br>
	}<br>
  
add the dependency<br>

 dependencies {<br>
	        implementation 'com.github.kimlinzd:VX_DATA:1.0.3'<br>
	}<br>
        

*开启服务：<br>
 开启服务会打开串口，并开始读取串口数据<br>
 activity?.let { it1 -> SerialService.startService(it1,"/dev/ttyS1",460800)}<br>

*停止服务：<br>
 停止服务会关闭串口，并停止读取数据的定时任务<br>
 activity?.let { it1 -> SerialService.stopService(it1) }<br>

*设置测试模式：<br>
 SerialPortManager.getInstance().setTestMode(true)<br>
 
 *****现在测试数据只有心电图数据*****

*接收串口数据使用LiveEventBus：<br>
 接收心电数据例子：<br>
 LiveEventBus.get(EventMsgConst.MsgEcgData1).observe(this, {<br>
                val data = it as EcgData1<br>
                LogUtils.d("接收到心电图信息",data.chn.toString())<br>
            }<br>
        )<br>
 
 其他数据：<br>
 血压NIBP：NibpData<br>
 血压NIBP（原始数据）:NibpOriginalData<br>
 呼吸数据:RespData<br>
 血氧SpO2数据:SpO2Data<br>
 血氧SpO2数据(原始数据)：SpO2OriginalData<br>
 体温数据：TempData<br>
 
 LiveEventBus key详见：EventMsgConst<br>
 
 *向串口写入数据（命令）<br>
  数据开始传输命令：<br>
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStart(),cmdReplyListener)<br>
  数据停止传输命令：<br>
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStop(),cmdReplyListener)<br>
  其他命令待完善<br>
        
        

      
