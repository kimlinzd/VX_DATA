package com.lepu.vx_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lepu.serial.manager.EcgDataSaveManager
import com.lepu.serial.manager.SerialPortManager
import com.lepu.serial.service.SerialService
import com.lepu.serial.uitl.FileUtil
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
            EcgDataSaveManager.getInstance().setPatitentId("45654315")
        }


        binding.buttonSerialStop.setOnClickListener{

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