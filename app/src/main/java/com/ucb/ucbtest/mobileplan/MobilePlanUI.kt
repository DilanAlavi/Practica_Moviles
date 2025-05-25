// app/src/main/java/com/ucb/ucbtest/mobileplan/MobilePlanUI.kt
package com.ucb.ucbtest.mobileplan

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucb.domain.MobilePlan
import com.ucb.ucbtest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobilePlanUI(
    viewModel: MobilePlanViewModel = hiltViewModel()
) {
    val plans by viewModel.plans.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlans()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestros planes móviles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B6B),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plans) { plan ->
                MobilePlanCard(plan = plan)
            }
        }
    }
}

@Composable
fun MobilePlanCard(
    plan: MobilePlan,
    onPlanSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val cardColor = Color(android.graphics.Color.parseColor(plan.color))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge "Nuestros planes móviles"
            Box(
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.9f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Nuestros planes móviles",
                    color = cardColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción del plan
            Text(
                text = "La mejor cobertura, increíbles beneficios y un compromiso con el planeta.",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nombre del plan
            Text(
                text = plan.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Precios
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Antes ",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${plan.originalPrice.toInt()}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = " /mes",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ahora ",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${plan.currentPrice.toInt()}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " /mes",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = plan.dataAmount,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Features
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social media icons (simuladas)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                repeat(6) { index ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icons = listOf("@", "f", "🐦", "📷", "in", "📺")
                        Text(
                            text = icons[index],
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN DE WHATSAPP Y ÍCONO - SOLUCIÓN IMPLEMENTADA POR EL USUARIO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón principal "Quiero este plan" con logo de WhatsApp
                Button(
                    onClick = {
                        // Navegar a la pantalla de SIM delivery
                        onPlanSelected(plan.name)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = cardColor
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Logo de WhatsApp en el botón
                        Image(
                            painter = painterResource(id = R.drawable.ic_whatsapp_logo),
                            contentDescription = "WhatsApp Logo",
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(cardColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Quiero este plan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Ícono de WhatsApp independiente (circular verde)
                IconButton(
                    onClick = {
                        val message = "Hola, UCB mobile, preciso su ayuda"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/${plan.whatsappNumber}?text=${Uri.encode(message)}")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Si WhatsApp no está instalado, abrir en navegador
                            val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://web.whatsapp.com/send?phone=${plan.whatsappNumber}&text=${Uri.encode(message)}")
                            }
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFF25D366), // Color verde oficial de WhatsApp
                            CircleShape
                        )
                ) {
                    // Logo de WhatsApp en el ícono circular
                    Image(
                        painter = painterResource(id = R.drawable.ic_whatsapp_logo),
                        contentDescription = "WhatsApp Icon",
                        modifier = Modifier.size(28.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            if (plan.isPopular) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " Plan más popular",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}