package com.example.delawaretrackandtraceapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.delawaretrackandtraceapp.domain.Package

class PackageTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromApiPackage(packageApi: Package): String {
        val type = object : TypeToken<Package>() {}.type
        return gson.toJson(packageApi, type)
    }

    @TypeConverter
    fun toApiPackage(packageString: String): Package {
        val type = object : TypeToken<Package>() {}.type
        return gson.fromJson(packageString, type)
    }
}
