package com.example.beaconactividad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beaconactividad.presentation.advertiser.AdvertiserScreen
import com.example.beaconactividad.presentation.scanner.ScannerScreen
import com.example.beaconactividad.presentation.viewmodel.BeaconViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeaconApp() {
    val viewModel: BeaconViewModel = viewModel()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Environment iBeacon Scanner") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("📡") },
                    label = { Text("Advertiser") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Text("📱") },
                    label = { Text("Scanner") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> AdvertiserScreen(viewModel)
                1 -> ScannerScreen(viewModel)
            }
        }
    }
}