package com.example.delawaretrackandtraceapp.domain

import java.util.UUID

class Item(
    val itemId : String = UUID.randomUUID().toString(),
    var quantity: Int,
    var product : Product,
    var totalPrice: Double = 0.0,
) {
     lateinit var order: Order
     lateinit var _package: Package

    override fun toString(): String {
        return "Product: " + this.product.productName + "\tAantal: " + this.quantity + "\tPrijs: " + this.totalPrice
    }
}