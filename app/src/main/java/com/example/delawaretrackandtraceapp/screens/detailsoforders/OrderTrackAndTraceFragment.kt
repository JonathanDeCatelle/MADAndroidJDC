package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentOrderSummaryBinding
import com.example.delawaretrackandtraceapp.databinding.FragmentOrderTrackAndTraceBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.*

class OrderTrackAndTraceFragment : Fragment() {

    private lateinit var binding: FragmentOrderTrackAndTraceBinding
    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_track_and_trace, container, false)

        viewModel.findOrder(arguments?.getString("OrderNr")!!)

        viewModel.dataOrder.observe(viewLifecycleOwner, Observer {
            with(binding.mapView2) {
                // Initialise the MapView
                onCreate(savedInstanceState)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync{
                    MapsInitializer.initialize(context)
                    setMap(it)
                }
            }
        })
        return binding.root
    }

    private val positionDelaware = LatLng(51.01372377623678, 3.735355755907398)
    private var locationDest: Address? = null
    private fun setMap(map: GoogleMap) {
        with(map) {
            addMarker(MarkerOptions().position(positionDelaware))
            val coder = context?.let { Geocoder(it) }
            try {
                val address = coder?.getFromLocationName(viewModel.dataOrder.value!!.deliveryAdress, 1)
                locationDest = address?.first()
                addMarker(MarkerOptions().position(LatLng(locationDest!!.latitude, locationDest!!.longitude)))
            } catch (e: Exception) {
                Log.e("Error Location", e.message.toString())
            }
            mapType = GoogleMap.MAP_TYPE_NORMAL
            showRoute(map)
        }
    }

    private fun showRoute(map: GoogleMap){
        //Define list to get all latlng for the route
        val path: MutableList<LatLng> = ArrayList()

        val geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyCNWXxbXGGgXdyrebMSQ3krFspkzxGyrHU")
            .build()

        val apiRequest = DirectionsApi.newRequest(geoApiContext)
        apiRequest.origin("Sluisweg 1 Gent")
        apiRequest.destination(viewModel.dataOrder.value!!.deliveryAdress)
        apiRequest.mode(TravelMode.DRIVING)

        var totalTimeRoute : Long = 0
        var totalTimeRouteString = ""


        try {
            val res: DirectionsResult = apiRequest.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.isNotEmpty()) {
                val route: DirectionsRoute = res.routes[0]
                if (route.legs != null) {
                    for (i in 0 until route.legs.size) {
                        val leg: DirectionsLeg = route.legs[i]

                        //Add time from route
                        totalTimeRoute += leg.duration.inSeconds
                        totalTimeRouteString += leg.duration.humanReadable

                        if (leg.steps != null) {
                            for (j in 0 until leg.steps.size) {
                                val step: DirectionsStep = leg.steps[j]
                                if (step.steps != null && step.steps.isNotEmpty()) {
                                    for (k in 0 until step.steps.size) {
                                        val step1: DirectionsStep = step.steps[k]
                                        val points1: EncodedPolyline = step1.polyline
                                        //Decode polyline and add points to list of route coordinates
                                        val coords1: MutableList<com.google.maps.model.LatLng>? = points1.decodePath()
                                        if (coords1 != null) {
                                            for (coord1 in coords1) {
                                                path.add(LatLng(coord1.lat, coord1.lng))
                                            }
                                        }
                                    }
                                } else {
                                    val points: EncodedPolyline = step.polyline
                                    //Decode polyline and add points to list of route coordinates
                                    val coords: MutableList<com.google.maps.model.LatLng>? = points.decodePath()
                                    if (coords != null) {
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.localizedMessage?.let { Log.e("Error", it) }
            Log.i("InfoFout", "Fout")
        }

        viewModel.setETATime(totalTimeRoute)

        //Draw the polyline
        if (path.isNotEmpty()) {
            val opts = PolylineOptions().addAll(path).color(Color.BLUE).width(6f)
            map.addPolyline(opts)
        }

        map.uiSettings.isZoomControlsEnabled = true
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng((positionDelaware.latitude + locationDest!!.latitude)/2, (positionDelaware.longitude + locationDest!!.longitude)/2), 11F))
    }
}