package com.lepu.serial.constant

/**
 * LiveEventBus key
 */
object EventMsgConst {

    /**
     * 采集原始数据
     */


    /**
     * 采集导联数据
     */
  //  const val MsgEcgData = "com.lepu.serial.obj.EcgData"

    /**
     *心电数据
     */
    const val MsgEcgData = "com.lepu.serial.obj.EcgData"
    /**
     *血压NIBP 实时袖带压（5Hz）
     */
    const val MsgNibpCP5HZData  = "com.lepu.serial.obj.NibpCP5HZData"
    /**
     *血压NIBP 袖带压原始数据（200Hz）
     */
    const val MsgNibpCP200HZData  = "com.lepu.serial.obj.NibpCP200HZData"
    /**
     *呼吸数据
     */
    const val MsgRespData  = "com.lepu.serial.obj.RespData"
    /**
     *SpO2数据
     */
    const val MsgSpO2Data  = "com.lepu.serial.obj.SpO2Data"
    /**
     *血氧SpO2  上传波形数据_原始数据
     */
    const val MsgSpO2WaveData  = "com.lepu.serial.obj.SpO2WaveData"
    /**
     *体温数据
     */
    const val MsgTempData = "com.lepu.serial.obj.TempData"
    /**
     *命令回复数据
     */
    const val  CmdReplyData = "com.lepu.serial.obj.CmdReply"
    /**
     * 血压参数和模块状态
     */
    const val NibpPramAndStatus="com.lepu.serial.obj.NibpPramAndStatus";
    /**
     *血压模块工作状态
     */
    const val NibpWorkingStatus="com.lepu.serial.obj.NibpWorkingStatus";
    /**
     * 血压模块信息
     */
    const val NibpModuleInfo="com.lepu.serial.obj.NibpModuleInfo";

    /**
     * 参数板复位  需要重新初始化
     */
    const val  ServeComReset="ServeComReset";

}