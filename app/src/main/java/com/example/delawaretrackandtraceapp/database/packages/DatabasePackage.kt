package com.example.delawaretrackandtraceapp.database.packages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.example.delawaretrackandtraceapp.domain.Package

@Entity(tableName = "packages")
data class DatabasePackage (
    @PrimaryKey(autoGenerate = false)
    var packageId: String = "",

    @ColumnInfo(name = "height")
    @Json(name = "height")
    var height: Int = 0,

    @ColumnInfo(name = "width")
    @Json(name = "width")
    var width: Int = 0,

    @ColumnInfo(name = "dept")
    @Json(name = "dept")
    var dept: Int = 0,

    @ColumnInfo(name = "price")
    @Json(name = "price")
    var price: Int = 0,
)

fun List<DatabasePackage>.asDomainModel(): List<Package>{
    return map {
        Package(
            packageId = it.packageId,
            height = it.height,
            width = it.width,
            dept = it.dept,
            price = it.price
        )
    }
}