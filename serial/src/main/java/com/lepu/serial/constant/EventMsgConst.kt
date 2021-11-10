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
     *血压NIBP 上传试试袖带压
     */
    const val MsgNibpData  = "com.lepu.serial.obj.NibpData"
    /**
     *血压NIBP 上传实时原始数据
     */
    const val MsgNibpOriginalData  = "com.lepu.serial.obj.NibpOriginalData"
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
    const val MsgSpO2OriginalData  = "com.lepu.serial.obj.SpO2OriginalData"
    /**
     *体温数据
     */
    const val MsgTempData = "com.lepu.serial.obj.TempData"
    /**
     *命令回复数据
     */
    const val  CmdReplyData = "com.lepu.serial.obj.CmdReply"

}