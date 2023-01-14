package com.example.delawaretrackandtraceapp.domain

class Supplier {
    private lateinit var supplierId: String
    private lateinit var name: String
    private lateinit var address: Address
    private lateinit var products: List<Product>
    private lateinit var users: List<User>
}