package com.example.beaconactividad.domain.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.util.Log
import androidx.annotation.RequiresPermission
import java.nio.ByteBuffer

class BeaconAdvertiser(
    private val bluetoothAdapter: BluetoothAdapter?
) {
    private val advertiser: BluetoothLeAdvertiser? = bluetoothAdapter?.bluetoothLeAdvertiser

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            Log.d("BeaconAdvertiser", "Advertising started successfully")
        }

        override fun onStartFailure(errorCode: Int) {
            Log.e("BeaconAdvertiser", "Advertising failed with error code: $errorCode")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    fun startAdvertising(temperature: Int, humidity: Int) {
        if (advertiser == null) return

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .setTimeout(0)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addManufacturerData(
                BeaconConstants.APPLE_COMPANY_ID,
                createIBeaconData(temperature, humidity)
            )
            .build()

        advertiser.startAdvertising(settings, data, advertiseCallback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
    fun stopAdvertising() {
        advertiser?.stopAdvertising(advertiseCallback)
    }

    private fun createIBeaconData(temperature: Int, humidity: Int): ByteArray {
        val buffer = ByteBuffer.allocate(23)

        // iBeacon prefix
        buffer.put(BeaconConstants.IBEACON_TYPE.toByte())
        buffer.put(BeaconConstants.IBEACON_LENGTH.toByte())

        // UUID
        buffer.put(BeaconConstants.ENVIRONMENT_UUID_BYTES)

        // Major: temperature (as is, no multiplication needed)
        buffer.putShort(temperature.toShort())

        // Minor: humidity (as is, no multiplication needed)
        buffer.putShort(humidity.toShort())

        // TX Power
        buffer.put(BeaconConstants.TX_POWER)

        return buffer.array()
    }

    fun isSupported(): Boolean = advertiser != null
}