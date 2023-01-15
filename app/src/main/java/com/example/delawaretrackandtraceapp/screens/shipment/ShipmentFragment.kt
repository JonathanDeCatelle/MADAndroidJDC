package com.example.delawaretrackandtraceapp.screens.shipment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentShipmentBinding
import com.example.delawaretrackandtraceapp.domain.*
import com.example.delawaretrackandtraceapp.screens.adres.AdresViewModel
import com.example.delawaretrackandtraceapp.screens.cart.CartViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopAdapter
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopListener
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopViewModel
import com.example.delawaretrackandtraceapp.screens.simpleshop.SimpleShopViewModelFactory
import java.util.Date
import java.util.UUID
import kotlin.math.ceil
import kotlin.math.max

class ShipmentFragment : Fragment() {
    private lateinit var binding: FragmentShipmentBinding
    private val shopViewModel: SimpleShopViewModel by activityViewModels() {
        val application = requireNotNull(this.activity).application
        val dataSource = ProductDatabase.getInstance(application).productDatabaseDao
        val adapter = SimpleShopAdapter(SimpleShopListener { })
        SimpleShopViewModelFactory(dataSource, application, adapter)
    }
    private val cartViewModel: CartViewModel by activityViewModels()
    private val orderViewModel: ListOfOrderViewModel by activityViewModels() {
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao
        ListOfOrderViewModelFactory(dataSource, application)
    }
    private lateinit var viewModel: ShipmentViewModel
    private val adresViewModel: AdresViewModel by activityViewModels()

    private lateinit var items : List<Product>
    private var totalPriceAlg: Double = 0.0
    private var height: Double = 0.0
    private var width: Double = 0.0
    private var dept: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Shipment Module"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipment, container, false)
        viewModel = ViewModelProvider(requireActivity())[ShipmentViewModel::class.java]
        items = cartViewModel.cart.value!!;

        binding.shipmentText.setText(
            adresViewModel.geefAdresAlsString()
        )

        binding.apply {
            shipmentTypeBoxSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Log.i("spinner test", parent?.selectedItemId.toString())
                        if (parent?.selectedItemId.toString() == "0") {
                            formaatDoosInvullen(breedteEdit, true)
                            formaatDoosInvullen(hoogteEdit, true)
                            formaatDoosInvullen(diepteEdit, true)
                            berekenPrijsSubtotaal()
                        } else if (parent?.selectedItemId.toString() == "1") {
                            formaatDoosInvullen(breedteEdit, false)
                            formaatDoosInvullen(hoogteEdit, false)
                            formaatDoosInvullen(diepteEdit, false)
                            berekenPrijsSubtotaal()
                        }
                    }
                }
        }

        binding.breedteEdit.addTextChangedListener(textWatcher)
        binding.hoogteEdit.addTextChangedListener(textWatcher)
        binding.diepteEdit.addTextChangedListener(textWatcher)

        binding.shipmentBestellingButton?.setOnClickListener { view: View ->
            if (binding.breedteEdit.text.toString() != "" || binding.hoogteEdit.text.toString() != ""|| binding.diepteEdit.text.toString() != "") {
                val handler = Handler()
                val ord = plaatsBestelling()
                handler.postDelayed({
                    val bundle = bundleOf("OrderNr" to ord.orderId)
                    view.findNavController().navigate(R.id.action_shipmentFragment_to_listOfOrderFragment, bundle)
                }, 1000)

                //val bundle = bundleOf("OrderNr" to ord.orderId)
                //view.findNavController().navigate(R.id.action_shipmentFragment_to_orderDetailFragment, bundle)
            } else {
                Toast.makeText(
                    context,
                    "Hoogte, breedte en diepte moeten ingevuld zijn.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun formaatDoosInvullen(et: EditText, standaard: Boolean) {
        binding.apply {
            if (standaard) {
                et.setText("50", TextView.BufferType.NORMAL)
                et.setBackgroundResource(android.R.color.transparent)
                et.isEnabled = false
            } else {
                et.setText("", TextView.BufferType.EDITABLE)
                et.isEnabled = true
                et.hint = "cm"
            }
        }
    }

    private fun berekenPrijsSubtotaal(): Int {
        var breedte: Int = 30
        var hoogte: Int = 50
        var diepte = 50

        val price = items.let { calculateNetPrice(it) };
        binding.apply {
            if (breedteEdit.text.toString() != "")
                breedte = breedteEdit.text.toString().toInt()
            if (hoogteEdit.text.toString() != "")
                hoogte = hoogteEdit.text.toString().toInt()
            if (diepteEdit.text.toString() != "")
                diepte = diepteEdit.text.toString().toInt()

            subtotaalPrijs.text = " €" + price.toString().take(5)
        }
        berekenPrijsNaSubtotaal(price, items, breedte,hoogte,diepte)

        return price.toInt()
    }

    fun calculateNetPrice(items: List<Product>): Double {
        return items.sumOf { it.unitPrice * it.aantalInCart }
    }
    private fun berekenPrijsNaSubtotaal(
        subPrice: Double,
        items: List<Product>,
        breedte: Int,
        hoogte: Int,
        diepte: Int
    ) {
        binding.errorText.text = ""

        height = hoogte.toDouble()
        width = breedte.toDouble()
        dept = diepte.toDouble()

        val palletPrijs = 10
        val prijs_verzending = 15
        val productAfmetingen = berekenGrootsteAfmetingen(items)
        Log.i("BBConfig", productAfmetingen.width.toString())
        Log.i("BBConfig", productAfmetingen.depth.toString())
        val doos = Doos(breedte, hoogte, diepte)
        val pallet = Pallet()
        /* -----------------------------------    */
        try {

            val hoeveelheidDozen = calculateNumberOfBoxes(items,doos,productAfmetingen)
            val hoeveelheidPaletten =  berekenHoeveelPaletten( doos, pallet, hoeveelheidDozen);

            Log.i("hoeveelheid", "dozen $hoeveelheidDozen")
            Log.i("hoeveelheid", "paletten $hoeveelheidPaletten")

            val dozenprijs = (hoeveelheidDozen * doos.price)
            val palettenprijs = (hoeveelheidPaletten *palletPrijs)

            val totalPrice = dozenprijs + palettenprijs + (1 * prijs_verzending) + subPrice

            binding.apply {
                //prijsBerekeningTotaal.removeAllViews()
                //prijsBerekeningTotaal.addView(
                makeTableRowFortotalCaluclation(
                    hoeveelheidDozen,
                    "dozen",
                    dozenprijs.toInt()
                )
                //)
                //prijsBerekeningTotaal.addView(
                makeTableRowFortotalCaluclation(
                    hoeveelheidPaletten,
                    "paletten",
                    palettenprijs
                )
                //)
                //prijsBerekeningTotaal.addView(
                makeTableRowFortotalCaluclation(
                    1,
                    "verzending",
                    prijs_verzending
                )
                //)
                totalPriceAlg = totalPrice
                totaalPrijs.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
                totaalPrijs.setTypeface(Typeface.DEFAULT_BOLD)
                totaalPrijs.text = "€ " +  totalPrice.toString().take(5)
            }
        }catch (e: Exception){
            binding.apply {
                errorText?.text = e.message.toString()
            }
        }finally {
        }
    }


    fun berekenHoeveelPaletten(doos: Doos, pallet: Pallet, aantalDozen: Int): Int {
        if (doos.width > pallet.width || doos.height > pallet.height || doos.depth > pallet.depth) {
            throw IllegalArgumentException("Deze doos past niet op het pallet")
        }

        val boxVolume = calculateBoxVolume(doos)
        val palletVolume = pallet.width * pallet.height * pallet.depth

        val boxesPerPallet = palletVolume / boxVolume

        val numberOfPallets = ceil((aantalDozen / boxesPerPallet).toDouble())

        return numberOfPallets.toInt()+1
    }


    fun berekenGrootsteAfmetingen(items: List<Product>): TransformProduct {
        var width = 0
        var height = 0
        var depth = 0
        for (item in items) {
            Log.i("BBC", item.productName)

            width = max(width, item.width.toInt())
            height = max(height, item.height.toInt())
            depth = max(depth, item.depth.toInt())
        }
        return TransformProduct(width, height, depth)
    }

    fun calculateNumberOfBoxes(items: List<Product>, doos: Doos, productDimensions: TransformProduct): Int {
        if (productDimensions.width > doos.width || productDimensions.height > doos.height || productDimensions.depth > doos.depth) {
            throw IllegalArgumentException("De producten passen niet in deze doos")
        }
        val totalVolume = items.sumOf{ (it.width * it.height * it.depth * it.aantalInCart) }
        println("Total volume: $totalVolume")
        val vol = calculateBoxVolume(doos)
        println("Total volume: $vol")
        var numBoxes = Math.ceil((totalVolume / calculateBoxVolume(doos)).toDouble()).toInt()

        val leftoverSpace = totalVolume % calculateBoxVolume(doos)
        if (leftoverSpace > 0) {
            var unplacedVolume = 0.0
            var currentBoxVolume = 0.0
            for (orderItem in items) {
                val itemVolume = orderItem.width* orderItem.height * orderItem.depth
                unplacedVolume += itemVolume * orderItem.aantalInCart
                currentBoxVolume += itemVolume * orderItem.aantalInCart

                if (currentBoxVolume > calculateBoxVolume(doos)) {
                    numBoxes++
                    currentBoxVolume = 0.0
                }
                if (unplacedVolume > leftoverSpace) {
                    break
                }
            }
            if (currentBoxVolume > 0) {
                numBoxes++
            }
        }
        println("Number of boxes: $numBoxes")
        return numBoxes
    }

    fun calculateBoxVolume(box: Doos): Int {
        return box.width * box.height * box.depth
    }
    private fun makeTableRowFortotalCaluclation(
        hoeveelheid: Int,
        name: String,
        price: Int
    ) {
        val zin = "+ $hoeveelheid $name"

        if (name == "paletten") {
            binding.palletten.text = zin
            binding.pallettenKost.text = "€ " + price.toString()
        } else if (name == "dozen") {
            binding.dozen.text = zin
            binding.dozenKost.text = "€ " + price.toString()
        } else if (name == "verzending") {
            binding.verzending.text = zin
            binding.verzendingKost.text = "€ " + price.toString()
        }


        val twPrice = TextView(context)
        twPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
        twPrice.text = "€ " + price.toString()
    }

    private fun plaatsBestelling(): OrderPost {
//        if (binding.editGemeente.text.toString() == "" || binding.editStraat.text.toString() == "" || binding.editHuisnummer.text.toString() == "")
//            Toast.makeText(context, "Afleveradres moet ingevuld worden", Toast.LENGTH_SHORT).show()
//        else {

        val newPackageId = UUID.randomUUID().toString().substring(0, 6)

        val newPackage = Package(
            newPackageId,
            height.toInt(),
            width.toInt(),
            dept.toInt(),
            totalPriceAlg.toInt()
        )

        val newOrderId = UUID.randomUUID().toString().substring(0, 6)

        val newOrder = OrderPost(
            newOrderId,
            totalPriceAlg,
            Date(),
            Date(),
            "1",
            adresViewModel.geefAdresAlsString(),
            adresViewModel.geefAdresAlsString(),
            "",
            newPackageId,
            0,
            totalPriceAlg,
        )

        var orderItems: List<OrderItem> = listOf()

        cartViewModel.cart.value?.forEach { p ->
            val newOrderItem = OrderItem(
                orderItemId = UUID.randomUUID().toString().substring(0, 6),
                quantity = p.aantalInCart,
                totalPrice = p.unitPrice * p.aantalInCart,
                productId = p.productId,
                orderId = newOrderId
            )

            orderItems += newOrderItem
        }

        orderViewModel.addOrder(newOrder, orderItems, newPackage)
        shopViewModel.resetCart()
        cartViewModel.resetCart()
        Toast.makeText(context, "Bestelling is geplaatst", Toast.LENGTH_SHORT).show()

        return newOrder
    }

    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            berekenPrijs()

        }
    }

    private fun berekenPrijs() {
        berekenPrijsSubtotaal()

    }

    class Pallet {
        val width: Int = 120
        val height: Int = 220
        val depth: Int = 80
        val price: Double = 10.0

        constructor()
    }

    class Doos(var width: Int, var height: Int, var depth: Int) {
        var price: Double = 1.5

    }

    class TransformProduct(val width: Int, val height: Int, val depth: Int)
}