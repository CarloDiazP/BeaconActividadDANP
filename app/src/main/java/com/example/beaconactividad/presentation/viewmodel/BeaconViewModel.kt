package com.example.beaconactividad.presentation.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beaconactividad.data.model.BeaconData
import com.example.beaconactividad.domain.ble.BeaconAdvertiser
import com.example.beaconactividad.domain.ble.BeaconScanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BeaconViewModel(application: Application) : AndroidViewModel(application) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    val advertiser = BeaconAdvertiser(bluetoothAdapter)
    val scanner = BeaconScanner(bluetoothAdapter) { beacon ->
        updateBeacon(beacon)
    }

    var isAdvertising by mutableStateOf(false)
        private set

    var isScanning by mutableStateOf(false)
        private set

    var temperature by mutableIntStateOf(25)
    var humidity by mutableIntStateOf(60)

    var beacons = mutableStateMapOf<String, BeaconData>()
        private set

    init {
        // Auto-clear old beacons
        viewModelScope.launch {
            while (true) {
                delay(5000)
                if (isScanning) {
                    clearOldBeacons()
                }
            }
        }
    }

    fun startAdvertising() {
        advertiser.startAdvertising(temperature, humidity)
        isAdvertising = true
    }

    fun stopAdvertising() {
        advertiser.stopAdvertising()
        isAdvertising = false
    }

    fun startScanning() {
        scanner.startScanning()
        isScanning = true
    }

    fun stopScanning() {
        scanner.stopScanning()
        isScanning = false
        beacons.clear()
    }

    private fun updateBeacon(beacon: BeaconData) {
        beacons[beacon.uuid] = beacon
    }

    private fun clearOldBeacons() {
        val currentTime = System.currentTimeMillis()
        beacons.entries.removeAll { currentTime - it.value.lastSeen > 10000 }
    }
}
