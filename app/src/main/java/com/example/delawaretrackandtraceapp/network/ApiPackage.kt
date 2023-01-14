package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.database.packages.DatabasePackage
import com.squareup.moshi.Json
import com.example.delawaretrackandtraceapp.domain.Package
import com.squareup.moshi.JsonClass

data class ApiPackageContainer (
    @Json(name = "packages")
    val apiPackages: List<ApiPackage>
    )

@JsonClass(generateAdapter = true)
data class ApiPackage(
    @Json(name = "packageId")
    var packageId: String = "",

    @Json(name = "height")
    var height: Int = 0,

    @Json(name = "width")
    var width: Int = 0,

    @Json(name = "dept")
    var dept: Int = 0,

    @Json(name = "price")
    var price: Int = 0,
)

fun ApiPackageContainer.asDomainModel(): List<Package>{
    return apiPackages.map {
        Package(
            packageId = it.packageId,
            height = it.height,
            width = it.width,
            dept = it.dept,
            price = it.price
        )
    }
}

fun ApiPackageContainer.asDatabaseModel(): Array<DatabasePackage>{
    return apiPackages.map {
        DatabasePackage(
            packageId = it.packageId,
            height = it.height,
            width = it.width,
            dept = it.dept,
            price = it.price
        )
    }.toTypedArray()
}