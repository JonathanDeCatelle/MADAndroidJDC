package com.example.delawaretrackandtraceapp.screens.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Delaware"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        binding.apply {
            shipmentButton.setOnClickListener{ view: View ->
                view.findNavController().navigate(R.id.action_startFragment_to_simpleShopFragment2)
            }

            notificationButton.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_startFragment_to_notificationsFragment)
            }

            ordertrackingButton.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_startFragment_to_listOfOrderFragment)
            }

            consoleBtn.setOnClickListener { view: View ->
                view.findNavController().navigate(R.id.action_startFragment_to_consoleFragment3)
            }
        }

        return binding.root
    }}

