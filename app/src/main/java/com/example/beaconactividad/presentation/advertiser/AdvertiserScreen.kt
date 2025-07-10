package com.example.beaconactividad.presentation.advertiser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beaconactividad.presentation.viewmodel.BeaconViewModel

@Composable
fun AdvertiserScreen(viewModel: BeaconViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EnvironmentDataCard(
            temperature = viewModel.temperature,
            humidity = viewModel.humidity,
            onTemperatureChange = { viewModel.temperature = it },
            onHumidityChange = { viewModel.humidity = it }
        )

        AdvertiseButton(
            isAdvertising = viewModel.isAdvertising,
            isSupported = viewModel.advertiser.isSupported(),
            onStartClick = { viewModel.startAdvertising() },
            onStopClick = { viewModel.stopAdvertising() }
        )

        if (viewModel.isAdvertising) {
            BroadcastingCard()
        }
    }
}

@Composable
private fun EnvironmentDataCard(
    temperature: Int,
    humidity: Int,
    onTemperatureChange: (Int) -> Unit,
    onHumidityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Environment Data",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Temperature: $temperature°C")
                Text("Humidity: $humidity%")
            }

            HorizontalDivider(thickness = 2.dp, color = Color.Gray)

            Column {
                Slider(
                    value = temperature.toFloat(),
                    onValueChange = { onTemperatureChange(it.toInt()) },
                    valueRange = -10f..50f,
                    steps = 59
                )
                Text("Temperature: $temperature°C")
            }

            Column {
                Slider(
                    value = humidity.toFloat(),
                    onValueChange = { onHumidityChange(it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 99
                )
                Text("Humidity: $humidity%")
            }
        }
    }
}

@Composable
private fun AdvertiseButton(
    isAdvertising: Boolean,
    isSupported: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Button(
        onClick = {
            if (isAdvertising) onStopClick() else onStartClick()
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = isSupported
    ) {
        Text(
            if (isAdvertising) "Stop Advertising" else "Start Advertising"
        )
    }
}

@Composable
private fun BroadcastingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            "Broadcasting iBeacon...",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
