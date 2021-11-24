package com.lepu.vx_data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lepu.serial.constant.SerialCmd
import com.lepu.serial.enums.*
import com.lepu.serial.manager.SerialPortManager
import com.lepu.serial.manager.SerialPortManager.CmdReplyListener
import com.lepu.serial.obj.CmdReply
import com.lepu.vx_data.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        //开始传输数据命令
        binding.buttonSerialStart.setOnClickListener {

            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdDataStart(), cmdReplyListener)
        }

        //停止传输命令
        binding.buttonSerialStop.setOnClickListener {
            SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStop(),
                cmdReplyListener)
        }
        //查询参数板信息
        binding.buttonGetParam.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdGetVersionInfo(),
                    cmdReplyListener)


        }
        //复位参数板
        binding.buttonReset.setOnClickListener{
            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdReset(),cmdReplyListener)
        }
        //设置病人类型
        binding.buttonSetPatientType.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdSetPatientType(PatientTypeEnum.ADULT),
                    cmdReplyListener)

        }
        //设置定标信号
        binding.buttonSetCal.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdSetCal(EcgCalEnum.CALCLOSE),
                    cmdReplyListener)

        }
        //设置窒息报警时间
        binding.buttonSetApneaTime.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdSetApneaDelay(100),
                    cmdReplyListener)

        }
        //测试数据
        binding.buttonTestTrue.setOnClickListener {
            SerialPortManager.getInstance().setTestMode(true)
        }
        //正式数据
        binding.buttonTestFalse.setOnClickListener {
            SerialPortManager.getInstance().setTestMode(false)
        }
        //设置导联
        binding.buttonSetLead.setOnClickListener {

            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdSetLeadMode(
                        EcgLeadModeEnum.LINE5,
                        EcgChn0IndexEnum.LEAD_I
                    ), cmdReplyListener)
        }
        //设置呼吸导联
        binding.buttonSetRespLead.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdSetRespLead(
                        RespLeadIndexEnum.LL
                    ), cmdReplyListener)
        }
        //参数板总参数
        binding.buttonSetTotalPram.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdSetParam(
                        PatientTypeEnum.ADULT,
                        EcgLeadModeEnum.LINE3,
                        EcgChn0IndexEnum.LEAD_I,
                        EcgCalEnum.CALOPEN,
                        RespLeadIndexEnum.LA,
                        10
                    ), cmdReplyListener)

        }


    }

    var cmdReplyListener: CmdReplyListener = object : CmdReplyListener {
        override fun onSuccess(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TYPE_RESET ->
                        Log.e("CmdReplyListener", "CMD_TYPE_RESET onSuccess")
                    CmdReply.CmdReplyType.CMD_VERSION_INFO ->
                        Log.e("CmdReplyListener", "CMD_VERSION_INFO onSuccess")
                    CmdReply.CmdReplyType.CMD_SET_PARAM  ->
                        Log.e("CmdReplyListener", "CMD_SET_PARAM  onSuccess")
                    CmdReply.CmdReplyType.CMD_PATIENT  ->
                        Log.e("CmdReplyListener", "CMD_PATIENT  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_START  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_DATA_START  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_STOP  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_DATA_STOP  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_ECG_LEAD_MODE  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_ECG_LEAD_MODE  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_CALIBRATION_SIGNAL  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_RESP_LEAD  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_RESP_LEAD  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_SUFFOCATION_ALARM_TIME  onSuccess")



                }
            }
        }

        override fun onFail(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TYPE_RESET ->
                        Log.e("CmdReplyListener", "CMD_TYPE_RESET onFail")
                    CmdReply.CmdReplyType.CMD_VERSION_INFO ->
                        Log.e("CmdReplyListener", "CMD_VERSION_INFO onFail")
                    CmdReply.CmdReplyType.CMD_SET_PARAM  ->
                        Log.e("CmdReplyListener", "CMD_SET_PARAM  onFail")
                    CmdReply.CmdReplyType.CMD_PATIENT  ->
                        Log.e("CmdReplyListener", "CMD_PATIENT  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_START  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_DATA_START  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_STOP  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_DATA_STOP  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_ECG_LEAD_MODE  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_ECG_LEAD_MODE  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_CALIBRATION_SIGNAL  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_RESP_LEAD  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_RESP_LEAD  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME  ->
                        Log.e("CmdReplyListener", "CMD_TYPE_SUFFOCATION_ALARM_TIME  onFail")

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}