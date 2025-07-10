package com.example.beaconactividad.domain.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import androidx.annotation.RequiresPermission
import com.example.beaconactividad.data.model.BeaconData
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and

class BeaconScanner(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val onBeaconFound: (BeaconData) -> Unit
) {
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private var scanCallback: ScanCallback? = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScanning() {
        if (scanner == null) return

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                parseResult(result)
            }
        }

        scanner.startScan(null, settings, scanCallback)
    }

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    fun stopScanning() {
        scanCallback?.let { scanner?.stopScan(it) }
    }

    private fun parseResult(result: ScanResult) {
        val scanRecord = result.scanRecord ?: return
        val manufacturerData = scanRecord.manufacturerSpecificData

        if (manufacturerData != null && manufacturerData.size() > 0) {
            for (i in 0 until manufacturerData.size()) {
                val key = manufacturerData.keyAt(i)
                val value = manufacturerData.get(key)

                if (key == BeaconConstants.APPLE_COMPANY_ID && value.size >= 23) {
                    parseIBeaconData(value, result.rssi)
                }
            }
        }
    }

    private fun parseIBeaconData(data: ByteArray, rssi: Int) {
        if (data.size < 23 ||
            data[0] != BeaconConstants.IBEACON_TYPE.toByte() ||
            data[1] != BeaconConstants.IBEACON_LENGTH.toByte()) {
            return
        }

        // Extract UUID
        val uuid = ByteBuffer.wrap(data, 2, 16).let { buffer ->
            val msb = buffer.long
            val lsb = buffer.long
            UUID(msb, lsb).toString()
        }

        // Extract Major (temperature)
        val major = ByteBuffer.wrap(data, 18, 2).short.toInt() and 0xFFFF
        val temperature = major

        // Extract Minor (humidity)
        val minor = ByteBuffer.wrap(data, 20, 2).short.toInt() and 0xFFFF
        val humidity = minor

        val beacon = BeaconData(
            uuid = uuid,
            major = major,
            minor = minor,
            rssi = rssi,
            temperature = temperature,
            humidity = humidity
        )

        onBeaconFound(beacon)
    }

    fun isSupported(): Boolean = scanner != null
}
