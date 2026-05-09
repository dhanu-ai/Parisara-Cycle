package com.example.frontend

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat

import com.example.frontend.model.Buddy
import com.example.frontend.model.DangerZone
import com.example.frontend.network.RetrofitInstance

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var locationPermissionRequest:
            androidx.activity.result.ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            locationPermissionRequest.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        setContent {

            MaterialTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    MapScreen()
                }
            }
        }
    }
}

@Composable
fun MapScreen() {

    val scope = rememberCoroutineScope()

    var buddies by remember {
        mutableStateOf<List<Buddy>>(emptyList())
    }

    var dangerZones by remember {
        mutableStateOf<List<DangerZone>>(emptyList())
    }

    val startLocation = LatLng(
        12.9716,
        77.5946
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            startLocation,
            14f
        )
    }

    LaunchedEffect(Unit) {

        scope.launch {

            while (true) {

                try {

                    buddies =
                        RetrofitInstance.api.getBuddies()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                delay(3000)
            }
        }

        scope.launch {

            while (true) {

                try {

                    dangerZones =
                        RetrofitInstance.api.getDangerZones()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                delay(10000)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),

            cameraPositionState = cameraPositionState,

            properties = MapProperties(
                isMyLocationEnabled = true
            )

        ) {

            dangerZones.forEach { zone ->

                Marker(
                    state = MarkerState(
                        position = LatLng(
                            zone.latitude,
                            zone.longitude
                        )
                    ),

                    title = zone.title,

                    snippet = zone.description
                )
            }

            buddies.forEach { buddy ->

                Marker(
                    state = MarkerState(
                        position = LatLng(
                            buddy.latitude,
                            buddy.longitude
                        )
                    ),

                    title = buddy.username,

                    snippet =
                        "Going to ${buddy.destination}"
                )
            }
        }
    }
}