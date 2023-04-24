package com.example.hikingbook.tool

import com.google.android.gms.maps.model.LatLng

fun LatLng.toStringRepresentation(): String {
    return "${this.latitude}, ${this.longitude}"
}

fun String.toLatLng(): LatLng? {
    return try {
        val latLngParts = this.split(", ")
        val latitude = latLngParts[0].toDouble()
        val longitude = latLngParts[1].toDouble()
        LatLng(latitude, longitude)
    } catch (e: Exception) {
        null
    }
}