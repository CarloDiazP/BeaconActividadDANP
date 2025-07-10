package com.example.beaconactividad.domain.ble

object BeaconConstants {
    const val APPLE_COMPANY_ID = 0x004C
    const val IBEACON_TYPE = 0x02
    const val IBEACON_LENGTH = 0x15
    const val TX_POWER = 0xB3.toByte()

    // Environmental iBeacon UUID: F7826DA6-4FA2-4E98-8024-BC5B71E0893E
    val ENVIRONMENT_UUID_BYTES = byteArrayOf(
        0xF7.toByte(), 0x82.toByte(), 0x6D.toByte(), 0xA6.toByte(),
        0x4F.toByte(), 0xA2.toByte(), 0x4E.toByte(), 0x98.toByte(),
        0x80.toByte(), 0x24.toByte(), 0xBC.toByte(), 0x5B.toByte(),
        0x71.toByte(), 0xE0.toByte(), 0x89.toByte(), 0x3E.toByte()
    )
}