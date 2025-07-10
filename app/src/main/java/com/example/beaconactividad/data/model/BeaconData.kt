package com.example.beaconactividad.data.model

data class BeaconData(
    val uuid: String,
    val major: Int,
    val minor: Int,
    val rssi: Int,
    val temperature: Int,
    val humidity: Int,
    val lastSeen: Long = System.currentTimeMillis()
)