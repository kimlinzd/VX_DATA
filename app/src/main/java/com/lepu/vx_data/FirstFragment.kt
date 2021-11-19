package com.lepu.vx_data

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lepu.serial.constant.SerialCmd
import com.lepu.serial.manager.SerialPortManager
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

        binding.buttonSerialStart.setOnClickListener{

            SerialPortManager.getInstance()
                .serialSendData(SerialCmd.cmdDataStart(), null)
        }


        binding.buttonSerialStop.setOnClickListener{
            SerialPortManager.getInstance().serialSendData(SerialCmd.cmdDataStop(),object :SerialPortManager.CmdReplyListener{
                override fun onSuccess(cmdType: Byte, connect: ByteArray?) {
                    Log.e("cmdDataStop","onSuccess");
                }

                override fun onFail(cmdType: Byte) {

                }

            })
         }

        binding.buttonGetParam.setOnClickListener{
            SerialPortManager.getInstance().serialSendData(SerialCmd.cmdInfo(),object :SerialPortManager.CmdReplyListener{
                override fun onSuccess(cmdType: Byte, connect: ByteArray?) {
                    val str = connect!!.toString(Charsets.UTF_8)
                    Log.e("cmdDataStop",str);
                }

                override fun onFail(cmdType: Byte) {

                }

            })
        }
        binding.buttonReset.setOnClickListener{
            SerialPortManager.getInstance().serialSendData(SerialCmd.cmdInfo(),object :SerialPortManager.CmdReplyListener{
                override fun onSuccess(cmdType: Byte, connect: ByteArray?) {
                 //   val str = connect!!.toString(Charsets.UTF_8)
                    Log.e("cmdInfo","cmdInfo");
                }

                override fun onFail(cmdType: Byte) {

                }

            })
        }


        binding.buttonTestTrue.setOnClickListener{
            SerialPortManager.getInstance().setTestMode(true)
        }

        binding.buttonTestFalse.setOnClickListener{
            SerialPortManager.getInstance().setTestMode(false)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}