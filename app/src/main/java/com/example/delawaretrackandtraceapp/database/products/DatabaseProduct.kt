package com.example.delawaretrackandtraceapp.database.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.delawaretrackandtraceapp.domain.Product
import com.squareup.moshi.Json

@Entity(tableName = "products")
data class DatabaseProduct(
    @PrimaryKey(autoGenerate = false)
    var productId: String = "",

    @ColumnInfo(name = "productCategoryId")
    @Json(name = "productCategoryId")
    var productCategoryId: Int = 0,

    @ColumnInfo(name = "productName")
    @Json(name = "name")
    var productName: String = "",

    @ColumnInfo(name = "unitPrice")
    @Json(name = "unitPrice")
    var unitPrice: Double = 0.0,

    @ColumnInfo(name = "supplierName")
    @Json(name = "supplierName")
    var supplierName: String = "",

    @ColumnInfo(name = "leftInStock")
    @Json(name = "leftInStock")
    var leftInStock: Int = 0,

    @ColumnInfo(name = "description")
    @Json(name = "description")
    var description: String = "",

    @ColumnInfo(name = "height")
    @Json(name = "height")
    var height: Double = 0.0,

    @ColumnInfo(name = "width")
    @Json(name = "width")
    var width: Double = 0.0,

    @ColumnInfo(name = "depth")
    @Json(name = "depth")
    var depth: Double = 0.0,
)

// convert Product to ApiProduct
fun List<DatabaseProduct>.asDomainModel() : List<Product>{
    return map {
        Product(
            productId = it.productId,
            productCategoryId = it.productCategoryId,
            productName = it.productName,
            unitPrice = it.unitPrice,
            supplierName = it.supplierName,
            lefInStock = it.leftInStock,
            description = it.description,
            height = it.height,
            width = it.width,
            depth = it.depth
        )
    }
}