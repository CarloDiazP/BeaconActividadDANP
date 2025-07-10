package com.example.beaconactividad.presentation.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beaconactividad.presentation.viewmodel.BeaconViewModel

@Composable
fun ScannerScreen(viewModel: BeaconViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScanButton(
            isScanning = viewModel.isScanning,
            isSupported = viewModel.scanner.isSupported(),
            onStartClick = { viewModel.startScanning() },
            onStopClick = { viewModel.stopScanning() }
        )

        BeaconList(
            beacons = viewModel.beacons.values.toList(),
            isScanning = viewModel.isScanning
        )
    }
}

@Composable
private fun ScanButton(
    isScanning: Boolean,
    isSupported: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Button(
        onClick = {
            if (isScanning) onStopClick() else onStartClick()
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = isSupported
    ) {
        Text(
            if (isScanning) "Stop Scanning" else "Start Scanning"
        )
    }
}

@Composable
private fun BeaconList(
    beacons: List<com.example.beaconactividad.data.model.BeaconData>,
    isScanning: Boolean
) {
    if (beacons.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (isScanning) "Scanning for beacons..." else "Press Start Scanning",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(beacons) { beacon ->
                BeaconCard(beacon)
            }
        }
    }
}