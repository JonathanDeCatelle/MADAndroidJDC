package com.example.delawaretrackandtraceapp.screens.productOverviewFromAPI

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.databinding.FragmentProductOverviewFromApiBinding

class ProductOverviewFromAPIFragment : Fragment() {

    private val viewModel: ProductOverviewFromAPIViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ProductOverviewFromAPIViewModel.Factory(activity.application))[ProductOverviewFromAPIViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding : FragmentProductOverviewFromApiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_overview_from_api, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

}