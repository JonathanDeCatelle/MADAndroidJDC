package com.example.delawaretrackandtraceapp.domain

class User {
    private lateinit var name: String
    private lateinit var userId: String
    private lateinit var emailaddress: String
    private lateinit var homeAddress: Address
    private lateinit var suppliers: List<Supplier>
    private lateinit var order: List<Order>
}