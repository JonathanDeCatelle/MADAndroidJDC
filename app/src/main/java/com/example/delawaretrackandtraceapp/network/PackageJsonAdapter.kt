package com.example.delawaretrackandtraceapp.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.example.delawaretrackandtraceapp.domain.Package
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class PackageJsonAdapter {

    @ToJson
    fun toJson(packageApi: Package): JsonElement {
        val json = JsonObject()
        json.addProperty("packageId", packageApi.packageId)
        json.addProperty("height", packageApi.height)
        json.addProperty("width", packageApi.width)
        json.addProperty("dept", packageApi.dept)
        json.addProperty("price", packageApi.price)
        return json
    }

    @FromJson
    fun fromJson(reader: JsonReader): Package {
        reader.beginObject()
        var packageId = ""
        var height = 0
        var width = 0
        var dept = 0
        var price = 0
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "packageId" -> packageId = reader.nextString()
                "height" -> height = reader.nextInt()
                "width" -> width = reader.nextInt()
                "dept" -> dept = reader.nextInt()
                "price" -> price = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Package(packageId, height, width, dept, price)
    }
}
