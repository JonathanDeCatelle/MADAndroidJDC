package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.database.products.DatabaseProduct
import com.example.delawaretrackandtraceapp.domain.Product
import com.squareup.moshi.Json

data class ApiProductContainer (
    @Json(name = "products")
    val apiProducts: List<ApiProduct>?
)

data class ApiProduct (
    @Json(name = "productId")
    var productId: String = "",

    @Json(name = "productCategoryId")
    var productCategoryId: Int = 0,

    @Json(name = "name")
    var productName: String = "",

    @Json(name = "unitPrice")
    var unitPrice: Double = 0.0,

    @Json(name = "leftInStock")
    var leftInStock: Int = 0,

    @Json(name = "description")
    var description: String = "",

    @Json(name = "height")
    var height: Double = 0.0,

    @Json(name = "width")
    var width: Double = 0.0,

    @Json(name = "depth")
    var depth: Double = 0.0,
)


fun ApiProductContainer.asDomainModel(): List<Product>{
    return apiProducts?.map {
        Product(
            productId = it.productId,
            productCategoryId = it.productCategoryId,
            productName = it.productName,
            unitPrice = it.unitPrice,
            supplierName = "",
            lefInStock = it.leftInStock,
            description = it.description,
            height = it.height,
            width = it.width,
            depth = it.depth
        )
    } ?: emptyList()
}

fun ApiProductContainer.asDatabaseModel(): Array<DatabaseProduct>{
    return apiProducts?.map {
        DatabaseProduct(
            productId = it.productId,
            productCategoryId = it.productCategoryId,
            productName = it.productName,
            unitPrice = it.unitPrice,
            supplierName = "",
            leftInStock = it.leftInStock,
            description = it.description,
            height = it.height,
            width = it.width,
            depth = it.depth
        )
    }?.toTypedArray() ?: emptyArray()
}
