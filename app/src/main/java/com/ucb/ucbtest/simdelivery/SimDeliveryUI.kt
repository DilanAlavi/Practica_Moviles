package com.ucb.ucbtest.simdelivery

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimDeliveryUI(
    viewModel: SimDeliveryViewModel = hiltViewModel(),
    onContinue: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var referencePhone by remember { mutableStateOf("") }
    var selectedLatitude by remember { mutableStateOf<Double?>(null) }
    var selectedLongitude by remember { mutableStateOf<Double?>(null) }
    var mapView by remember { mutableStateOf<MapView?>(null) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = "UCBTest"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Envío de SIM") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B6B),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Donde enviaremos tu SIM",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF2C2C2C)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(3.dp)
                            .background(Color(0xFFFF6B6B))
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = referencePhone,
                        onValueChange = { referencePhone = it },
                        label = { Text("Teléfono de referencia") },
                        placeholder = { Text("Ej: +591 70123456") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFFF6B6B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF6B6B),
                            focusedLabelColor = Color(0xFFFF6B6B)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedLatitude?.toString() ?: "",
                            onValueChange = { },
                            label = { Text("Latitud") },
                            modifier = Modifier.weight(1f),
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFF6B6B)
                            )
                        )
                        OutlinedTextField(
                            value = selectedLongitude?.toString() ?: "",
                            onValueChange = { },
                            label = { Text("Longitud") },
                            modifier = Modifier.weight(1f),
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFF6B6B)
                            )
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🗺️ Toca el mapa para seleccionar ubicación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C2C2C)
                        )
                    }

                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            MapView(context).apply {
                                mapView = this
                                setupOSMMap(this, context) { lat, lng ->
                                    selectedLatitude = lat
                                    selectedLongitude = lng
                                }
                            }
                        }
                    )
                }
            }

            // Botón justo después del mapa
            Button(
                onClick = {
                    if (selectedLatitude != null && selectedLongitude != null) {
                        viewModel.saveDeliveryInfo(
                            referencePhone = referencePhone,
                            latitude = selectedLatitude!!,
                            longitude = selectedLongitude!!
                        )
                        onContinue()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = referencePhone.isNotBlank() && selectedLatitude != null && selectedLongitude != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B6B),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Ubicación seleccionada
            if (selectedLatitude != null && selectedLongitude != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📍 Ubicación seleccionada",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "Lat: ${String.format("%.6f", selectedLatitude!!)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Lng: ${String.format("%.6f", selectedLongitude!!)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "🗺️ Powered by OpenStreetMap",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Estado de carga
            when (val currentState = uiState) {
                is SimDeliveryViewModel.UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFF6B6B))
                    }
                }
                is SimDeliveryViewModel.UiState.Success -> {
                    Text(
                        text = "✅ Información de envío guardada correctamente",
                        color = Color(0xFF4CAF50),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is SimDeliveryViewModel.UiState.Error -> {
                    Text(
                        text = "❌ ${currentState.message}",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> {}
            }
        }
    }
}

// Configuración del mapa
private fun setupOSMMap(
    mapView: MapView,
    context: Context,
    onLocationSelected: (Double, Double) -> Unit
) {
    mapView.apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)

        val cochabambaPoint = GeoPoint(-17.3935, -66.1570)
        controller.setZoom(13.0)
        controller.setCenter(cochabambaPoint)

        setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val projection = mapView.projection
                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint

                overlays.clear()
                val marker = Marker(mapView).apply {
                    position = geoPoint
                    title = "Ubicación seleccionada"
                    snippet = "Lat: ${String.format("%.6f", geoPoint.latitude)}\nLng: ${String.format("%.6f", geoPoint.longitude)}"
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                overlays.add(marker)
                onLocationSelected(geoPoint.latitude, geoPoint.longitude)
                invalidate()
            }
            false
        }
    }
}
