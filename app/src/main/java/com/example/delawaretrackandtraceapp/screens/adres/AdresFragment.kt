package com.example.delawaretrackandtraceapp.screens.adres

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.databinding.FragmentAdresBinding


class AdresFragment : Fragment() {
    private lateinit var binding: FragmentAdresBinding
    private val viewModel: AdresViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Shipment Module"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_adres, container, false)
        binding.adres = viewModel

        binding.adresIngevuld.setOnClickListener{ view: View ->

         var ingevuld = viewModel.leesFormulierIn(binding)
            if (ingevuld) {
                view.findNavController().navigate(R.id.action_adresFragment_to_shipmentFragment)
            } else {
                Toast.makeText(context, "Alle velden moeten ingevuld zijn", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}

