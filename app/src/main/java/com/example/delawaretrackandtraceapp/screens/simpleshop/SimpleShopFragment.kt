package com.example.delawaretrackandtraceapp.screens.simpleshop

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentSimpleShopBinding
import com.example.delawaretrackandtraceapp.domain.Product

class SimpleShopFragment : Fragment() {
    lateinit var binding: FragmentSimpleShopBinding
    lateinit var viewModel: SimpleShopViewModel
    lateinit var adapter: SimpleShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Shipment Module"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_shop, container, false)

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = ProductDatabase.getInstance(application).productDatabaseDao

        adapter = SimpleShopAdapter(SimpleShopListener {
            product ->
            if(product.aantal > 0) {
                Toast.makeText(
                    context,
                    product.aantal.toString() + " keer " + product.productName + " toegevoegd",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addToCart(product)
            }else {
                Toast.makeText(
                    context,
                    "Aantal moet ingevuld zijn en kan niet 0 zijn",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding.productList.adapter = adapter

        val viewModelFactory = SimpleShopViewModelFactory(dataSource, application, adapter)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SimpleShopViewModel::class.java)

        binding.shop = viewModel
        binding.lifecycleOwner = this

        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.bekijkWinkelwagen.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_simpleShopFragment_to_cartFragment)
        }

        return binding.root
    }

    fun testFunc(inflater: LayoutInflater, container: ViewGroup?,
                 savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple_shop, container, false)
        binding.shop = viewModel
        viewModel.addToCart(Product("test", 0, "test test", 10.0, "testSupplier", 2, "Test", 10.0, 10.0, 10.0))
        return binding.root
}
}

