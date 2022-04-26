package com.lepu.vx_data

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lepu.serial.constant.SerialCmd
import com.lepu.serial.enums.*
import com.lepu.serial.listener.CmdNibpReplyListener
import com.lepu.serial.listener.CmdReplyListener
import com.lepu.serial.manager.SerialPortManager
import com.lepu.serial.obj.CmdReply
import com.lepu.vx_data.databinding.FragmentFirstBinding
import android.widget.Toast




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
    //   SerialPortManager.getInstance().setTestMode(true);
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


        }
        //设置定标符号
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
            SerialPortManager.getInstance().setModel(ModelEnum.MODEL_TEST)
        }
        //正式数据
        binding.buttonTestFalse.setOnClickListener {
            SerialPortManager.getInstance().setModel(ModelEnum.MODEL_NORMAL)
        }


        //设置导联模式
        binding.buttonSetLead.setOnClickListener {

            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdSetLeadMode(
                        EcgLeadModeEnum.LINE3,
                        EcgChn0IndexEnum.LEAD_III
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
                    ), cmdReplyListener
                )

        }

        // 开始手动血压测量
        binding.buttonStartBp.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdStartManualBp(), cmdNibpReplyListener
                )

        }

        // 开始连续测量
        binding.buttonContinuousBp.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdStartContinuousBp(), cmdNibpReplyListener
                )

        }

        // 取消测量
        binding.buttonCancelBp.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdCancelBp(), cmdNibpReplyListener
                )

        }


        // 血压 设置病人
        binding.buttonBpSetPatient.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpSetPatient(PatientTypeEnum.ADULT), cmdNibpReplyListener
                )

        }

        //  设置初始充气压
        binding.buttonBpSetInflated.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpSetInitialPressure(230), cmdNibpReplyListener
                )

        }

        // 血压设置传输模式
        binding.buttonSetTransferMode.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpSetTransferMode(NipbpWmEnum.HZ_200), cmdNibpReplyListener
                )

        }
        // 读取血压参数
        binding.buttonReadBpParam.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpReadBpParam(), cmdNibpReplyListener
                )

        }

        // 主动读取血压模块工作状态
        binding.buttonReadBpWorkStatus.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpReadBpWorkStatus(), cmdNibpReplyListener
                )

        }

        // 读取血压模块信息
        binding.buttonReadBpModuleInfo.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpReadBpModuleInfo(), cmdNibpReplyListener
                )

        }

        //  控制泵
        binding.buttonReadBpControlPump.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpControlPump(50), cmdNibpReplyListener
                )

        }

        //  控制快泵
        binding.buttonReadBpControlQuickValve.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpControlQuickValve(NibpValveControlEnum.VALVE_OPEN),
                    cmdNibpReplyListener
                )

        }

        //  控制慢泵
        binding.buttonReadBpControlSlowValve.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpControlQSlowValve(NibpValveControlEnum.VALVE_OPEN),
                    cmdNibpReplyListener
                )

        }

        //  睡眠模式
        binding.buttonNibpSetSleepMode.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpSetSleepMode(), cmdNibpReplyListener
                )

        }

        //  复位模块
        binding.buttonNibpSetReset.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpSetResetModule(), cmdNibpReplyListener
                )

        }

        //辅助静脉穿刺
        binding.buttonNibpAuxiliaryVenipuncture.setOnClickListener {
            Log.e("lzd","辅助静脉穿刺")
          /*  SerialPortManager.getInstance().serialSendData( SerialCmd.cmdNibpAuxiliaryVenipuncture(
                160)
                )*/
            SerialPortManager.getInstance().serialSendData(SerialCmd.cmdNibpAuxiliaryVenipuncture(
                80
            ))
        }

        // 压力校验模式1（内部充气源）
        binding.buttonCalibrationMode1.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpCalibrationMode1(230), cmdNibpReplyListener
                )

        }

        // 压力校验模式2（外部充气源）
        binding.buttonCalibrationMode2.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpCalibrationMode2(), cmdNibpReplyListener
                )

        }


        //漏气检测
        binding.buttonLeakDetection.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpLeakDetection(), cmdNibpReplyListener
                )

        }

        // 校准指令
        binding.buttonCalibration.setOnClickListener {
            SerialPortManager.getInstance()
                .serialSendData(
                    SerialCmd.cmdNibpCalibration(NibpCalibrationMode.CALIBRATION,250), cmdNibpReplyListener
                )

        }


       binding.buttonNibpStatus.setOnClickListener {
          Log.e("nibp", SerialPortManager.getInstance().nibpInfo.nibpMsmEnum.toString())

       }



    }

    var cmdReplyListener: CmdReplyListener = object : CmdReplyListener {
        override fun onSuccess(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TYPE_RESET ->
                        Log.e("nibp", "CMD_TYPE_RESET onSuccess")
                    CmdReply.CmdReplyType.CMD_VERSION_INFO ->
                        Log.e("nibp", "CMD_VERSION_INFO onSuccess")
                    CmdReply.CmdReplyType.CMD_SET_PARAM  ->
                        Log.e("nibp", "CMD_SET_PARAM  onSuccess")
                    CmdReply.CmdReplyType.CMD_PATIENT  ->
                        Log.e("nibp", "CMD_PATIENT  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_START  ->
                        Log.e("nibp", "CMD_TYPE_DATA_START  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_STOP  ->
                        Log.e("nibp", "CMD_TYPE_DATA_STOP  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_ECG_LEAD_MODE  ->
                        Log.e("nibp", "CMD_TYPE_ECG_LEAD_MODE  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL  ->
                        Log.e("nibp", "CMD_TYPE_CALIBRATION_SIGNAL  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_RESP_LEAD  ->
                        Log.e("nibp", "CMD_TYPE_RESP_LEAD  onSuccess")
                    CmdReply.CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME  ->
                        Log.e("nibp", "CMD_TYPE_SUFFOCATION_ALARM_TIME  onSuccess")
                }
            }
        }

        override fun onFail(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TYPE_RESET ->
                        Log.e("nibp", "CMD_TYPE_RESET onFail")
                    CmdReply.CmdReplyType.CMD_VERSION_INFO ->
                        Log.e("nibp", "CMD_VERSION_INFO onFail")
                    CmdReply.CmdReplyType.CMD_SET_PARAM  ->
                        Log.e("nibp", "CMD_SET_PARAM  onFail")
                    CmdReply.CmdReplyType.CMD_PATIENT  ->
                        Log.e("nibp", "CMD_PATIENT  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_START  ->
                        Log.e("nibp", "CMD_TYPE_DATA_START  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_STOP ->
                        Log.e("nibp", "CMD_TYPE_DATA_STOP  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_ECG_LEAD_MODE ->
                        Log.e("nibp", "CMD_TYPE_ECG_LEAD_MODE  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL ->
                        Log.e("nibp", "CMD_TYPE_CALIBRATION_SIGNAL  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_RESP_LEAD ->
                        Log.e("nibp", "CMD_TYPE_RESP_LEAD  onFail")
                    CmdReply.CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME ->
                        Log.e("nibp", "CMD_TYPE_SUFFOCATION_ALARM_TIME  onFail")

                }
            }
        }

        override fun onTimeOut(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TYPE_RESET ->
                        Log.e("nibp", "CMD_TYPE_RESET onTimeOut")
                    CmdReply.CmdReplyType.CMD_VERSION_INFO ->
                        Log.e("nibp", "CMD_VERSION_INFO onTimeOut")
                    CmdReply.CmdReplyType.CMD_SET_PARAM ->
                        Log.e("nibp", "CMD_SET_PARAM  onTimeOut")
                    CmdReply.CmdReplyType.CMD_PATIENT ->
                        Log.e("nibp", "CMD_PATIENT  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_START ->
                        Log.e("nibp", "CMD_TYPE_DATA_START  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_DATA_STOP ->
                        Log.e("nibp", "CMD_TYPE_DATA_STOP  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_ECG_LEAD_MODE ->
                        Log.e("nibp", "CMD_TYPE_ECG_LEAD_MODE  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_CALIBRATION_SIGNAL ->
                        Log.e("nibp", "CMD_TYPE_CALIBRATION_SIGNAL  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_RESP_LEAD ->
                        Log.e("nibp", "CMD_TYPE_RESP_LEAD  onTimeOut")
                    CmdReply.CmdReplyType.CMD_TYPE_SUFFOCATION_ALARM_TIME ->
                        Log.e("nibp", "CMD_TYPE_SUFFOCATION_ALARM_TIME  onTimeOut")

                }
            }

        }
    }

    var cmdNibpReplyListener: CmdNibpReplyListener = object : CmdNibpReplyListener {
        override fun obtain_O(cmdReply: CmdReply?) {

            Log.e(
                "obtain_O",
                "obtain_O "
            )

           // 0x01，0x02，0x04，0x05，0x0A，0x0B，0x0C，0x0E，0x0F，0x10，0x11，0x12
        }

        override fun obtain_K(cmdReply: CmdReply?) {

        }

        override fun obtain_B(cmdReply: CmdReply?) {

        }

        override fun obtain_A(cmdReply: CmdReply?) {

        }

        override fun obtain_N(cmdReply: CmdReply?) {

        }

        override fun obtain_S(cmdReply: CmdReply?) {

        }

        override fun obtain_R(cmdReply: CmdReply?) {

        }

        override fun onFail(cmdReply: CmdReply?) {


        }

        override fun onTimeOut(cmdReply: CmdReply?) {
            if (cmdReply != null) {
                when (cmdReply.cmdReplyType) {
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT ->
                        Log.e(
                            "cmdNibpReplyListener",
                            "CMD_TOKEN_NIBP_START_MANUAL_BLOOD_PRESSURE_MEASUREMENT  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_START_CONTINUOUS_MEASUREMENT  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_CANCEL_MEASUREMENT ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_CANCEL_MEASUREMENT  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_SET_PATIENT_TYPE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_SET_PATIENT_TYPE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_SET_INITIAL_INFLATION_PRESSURE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_SET_WAVE_TRANSMISSION_MODE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_PARAMETERS  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_READ_THE_WORKING_STATUS_OF_THE_BLOOD_PRESSURE_MODULE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_READ_BLOOD_PRESSURE_MODULE_INFO  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_CONTROL_PUMP ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_CONTROL_PUMP  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_CONTROL_QUICK_VALVE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_CONTROL_QUICK_VALVE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_CONTROL_SLOW_VALVE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_CONTROL_SLOW_VALVE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_SLEEP_MODE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_SLEEP_MODE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_RESET_MODULE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_RESET_MODULE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_RESET_MODULE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_RESET_MODULE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_AUXILIARY_VENIPUNCTURE  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1 ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_1  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2 ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_PRESSURE_CALIBRATION_MODE_2  onTimeOut"
                        )
                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_LEAK_DETECTION ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_LEAK_DETECTION  onTimeOut"
                        )

                    CmdReply.CmdReplyType.CMD_TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR  ->
                        Log.e(
                            "cmdStartContinuousBp",
                            "CMD_TOKEN_NIBP_CALIBRATE_THE_PRESSURE_SENSOR  onTimeOut"
                        )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}