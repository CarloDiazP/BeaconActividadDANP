package com.example.beaconactividad.presentation.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beaconactividad.data.model.BeaconData

@Composable
fun BeaconCard(beacon: BeaconData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "UUID: ${beacon.uuid}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Major: ${beacon.major}")
                Text("Minor: ${beacon.minor}")
                Text("RSSI: ${beacon.rssi} dBm")
            }
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    "🌡️ ${beacon.temperature}°C",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "💧 ${beacon.humidity}%",
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
