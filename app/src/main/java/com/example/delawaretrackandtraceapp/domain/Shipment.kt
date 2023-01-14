package com.example.delawaretrackandtraceapp.domain

import java.util.Date

class Shipment {
    private lateinit var shipmentId: String
    private lateinit var estimatedArrivalTime: Date
    private lateinit var departuretime: Date
    private var shipmentStatus: Int = 0
    private lateinit var departureAddress: Address
    private lateinit var deliveryAddress: Address
    private lateinit var order: List<Order>
}