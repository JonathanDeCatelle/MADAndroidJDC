package com.example.delawaretrackandtraceapp.screens.cart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentCartBinding
import com.example.delawaretrackandtraceapp.screens.detailsoforders.DetailViewModel
import com.example.delawaretrackandtraceapp.screens.detailsoforders.DetailViewModelFactory
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopAdapter
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopListener
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopViewModel
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopViewModelFactory

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel
    private lateinit var shopViewModel: SimpleShopViewModel //by activityViewModels()
    private lateinit var binding: FragmentCartBinding
    lateinit var adapter: SimpleShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = ProductDatabase.getInstance(application).productDatabaseDao

        adapter = SimpleShopAdapter(SimpleShopListener { })

        val viewModelFactory = SimpleShopViewModelFactory(dataSource, application, adapter)
        shopViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[SimpleShopViewModel::class.java]

        viewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        viewModel.setCart(shopViewModel.cart)

        binding.apply {
            bestelProducten.setOnClickListener { view: View ->
                if (viewModel.cart.value?.isEmpty() == true || viewModel.cart.value == null){
                    Toast.makeText(context, "De winkelwagen is leeg", Toast.LENGTH_SHORT).show()
                }else{
                    view.findNavController().navigate(R.id.action_cartFragment_to_adresFragment)
                }
            }
        }

        showProductsInCart()

        return binding.root
    }

    private fun showProductsInCart(){
        shopViewModel.cart.value?.forEach {
            val parent = LinearLayout(ContextThemeWrapper(context, R.style.productcartlayout))
            val tvNaam = TextView(context, null, 0, R.style.productcart)
            val tvAantal = TextView(context, null, 0, R.style.productcart)

            tvAantal.text = "${it.aantalInCart} - "
            tvNaam.text = "${it.productName}"

            parent.addView(tvAantal)
            parent.addView(tvNaam)
            binding.cartProducts.addView(parent)
        }
        binding.totaalWinkelwagen.text = "Totaal: €" + shopViewModel.calculateOffWholeCart().toString()
    }
}