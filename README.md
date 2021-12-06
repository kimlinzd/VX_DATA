# VX_DATA
AiView android端使用的接口工具 包括打开串口，处理串口数据，向串口写入命令。

***所有业务命令demo写在FirstFragment里面***

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
        

*初始化：
 初始化会打开串口，并开始读取串口数据
SerialPortManager.getInstance().init(this, "/dev/ttyS1", 460800, object :
        SerialConnectListener {
            override fun onSuccess() {}
            override fun onFail() {}
        })

*关闭串口：
 关闭串口会关闭串口，并停止读取数据的定时任务
SerialPortManager.getInstance().closeSerialPort()

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
 血压NIBP 袖带压5HZ：NibpCP5HZData
 血压NIBP 实时原始数据（200Hz）:NibpCP200HZData
 血压模块信息： NibpModuleInfo 
 血压参数和模块状态：NibpPramAndStatus 
 血压模块工作状态：NibpWorkingStatus 
 呼吸数据:RespData
 血氧SpO2数据:SpO2Data
 血氧SpO2数据(原始数据)：SpO2OriginalData
 体温数据：TempData
 
 LiveEventBus key详见：EventMsgConst
 
*向串口写入数据（命令）
  详情见 串口命令合成类：SerialCmd

  以下cmdReplyListener监听（NIBP命令除外）：
  public interface CmdReplyListener {
  //请求成功
  void onSuccess(CmdReply cmdReply);
  //请求失败
  void onFail(CmdReply cmdReply);
  //请求超时
  void onTimeOut(CmdReply cmdReply);
  }

  可通过cmdReply.cmdReplyType来判断是哪一个命令的回复

  //参数板整体业务

  数据开始传输命令：
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStart(),cmdReplyListener)
  数据停止传输命令：
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStop(),cmdReplyListener)
  复位参数板命令：
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdReset(),cmdReplyListener)
  查询参数板信息:      
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdGetVersionInfo(),cmdReplyListener)
  设置病人类型:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetPatientType(PatientTypeEnum.ADULT), cmdReplyListener)
  参数板总参数
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetParam(PatientTypeEnum.ADULT, EcgLeadModeEnum.LINE3,
  EcgChn0IndexEnum.LEAD_I, EcgCalEnum.CALOPEN, RespLeadIndexEnum.LA, 10), cmdReplyListener)
  
  //心电ECG业务
  设置导联模式:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetLeadMode(EcgLeadModeEnum.LINE5,
  EcgChn0IndexEnum.LEAD_I), cmdReplyListener)
  设置定标符号:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetCal(EcgCalEnum.CALCLOSE), cmdReplyListener)
  
  //呼吸RESP业务
  设置呼吸导联:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetRespLead(RespLeadIndexEnum.LL), cmdReplyListener)
  设置窒息报警时间:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdSetApneaDelay(100), cmdReplyListener)

  //体温TEMP业务
  体温业务暂时没有



  //血压NIBP业务
  
  血压命令回复: 可通过cmdReply.cmdReplyType来判断是哪一个命令的回复
  ‘O’包	0x4F	表示接收到下发的指令,支持响应“O”的命令类型type = 0x01，0x02，0x04，0x05，0x0A，0x0B，0x0C，0x0E，0x0F，0x10，0x11，0x12
  ‘K’包	0x4B	表示接收到的指令已经完成执行,支持响应“K”的命令类型type = 0x01，0x02，0x0F，0x10，0x11，0x12
  ‘B’包	0x42	表示正在执行其它的命令，或者正在自动归零,可能会遇到响应“B”的命令类型type = 0x01，0x02，0x04，0x05，0x0A，0x0B，0x0C，0x0E，0x0F，0x10，0x11，0x12
  ‘A’包	0x41	表示接收到取消测量的指令,支持响应“A”的命令类型type = 0x03
  ‘N’包	0x4E	表示接收到的包是无效的，所有只是校验码 错了,或者是参数（数据）不在可设置范围内的命令都会响应“N”包
  ‘S’包	0x53	表示模块进入休眠，支持响应“S”的命令类型type = 0x0D，在模块主动进入睡眠时，也会向上 发送
  ‘R’包	0x52	表示模块软件复位，支持响应“R”的命令类型type = 0x0E
  public interface CmdNibpReplyListener {
    void obtain_O(CmdReply cmdReply);
    void obtain_K(CmdReply cmdReply);
    void obtain_B(CmdReply cmdReply);
    void obtain_A(CmdReply cmdReply);
    void obtain_N(CmdReply cmdReply);
    void obtain_S(CmdReply cmdReply);
    void obtain_R(CmdReply cmdReply);
    //请求失败
    void onFail(CmdReply cmdReply);
    //超时
    void onTimeOut(CmdReply cmdReply);
  }

  开始手动血压测量:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdStartManualBp(), cmdNibpReplyListener)
  开始连续测量:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdStartContinuousBp(), cmdNibpReplyListener)
  取消测量:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdCancelBp(), cmdNibpReplyListener)
  血压 设置病人:
  SerialPortManager.getInstance().serialSendData(
  SerialCmd.cmdNibpSetPatient(PatientTypeEnum.ADULT), cmdNibpReplyListener)
  设置初始充气压:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpSetPatient(270), cmdNibpReplyListener)
  血压设置传输模式:
  SerialPortManager.getInstance().serialSendData(
  SerialCmd.cmdNibpSetTransferMode(NipbpWmEnum.HZ_200), cmdNibpReplyListener)
  读取血压参数:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpReadBpParam(), cmdNibpReplyListener)
  主动读取血压模块工作状态:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpReadBpWorkStatus(), cmdNibpReplyListener)
  读取血压模块信息:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpReadBpModuleInfo(), cmdNibpReplyListener)
  控制泵:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpControlPump(50), cmdNibpReplyListener)
  控制快泵:
  SerialPortManager.getInstance().serialSendData(
  SerialCmd.cmdNibpControlQuickValve(NibpValveControlEnum.VALVE_OPEN), cmdNibpReplyListener)
  控制慢泵:
  SerialPortManager.getInstance()
  .serialSendData(SerialCmd.cmdNibpControlQSlowValve(NibpValveControlEnum.VALVE_OPEN), cmdNibpReplyListener)
  睡眠模式:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpSetSleepMode(), cmdNibpReplyListener)
  复位模块:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpSetResetModule(), cmdNibpReplyListener)
  辅助静脉穿刺:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpAuxiliaryVenipuncture(230), cmdNibpReplyListener)
  压力校验模式1（内部充气源）:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpCalibrationMode1(230), cmdNibpReplyListener)
  压力校验模式2（外部充气源）:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpCalibrationMode2(), cmdNibpReplyListener)
  漏气检测:
  SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpLeakDetection(), cmdNibpReplyListener)
  校准指令:
  SerialPortManager.getInstance()
  .serialSendData(SerialCmd.cmdNibpCalibration(NibpCalibrationMode.CALIBRATION,250), cmdNibpReplyListener)
  
