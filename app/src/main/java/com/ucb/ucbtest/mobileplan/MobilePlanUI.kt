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
fun MobilePlanCard(plan: MobilePlan) {
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
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
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

            Text(
                text = "La mejor cobertura, increíbles beneficios y un compromiso con el planeta.",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = plan.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Antes ", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text(
                        text = "$${plan.originalPrice.toInt()}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(" /mes", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ahora ", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = "$${plan.currentPrice.toInt()}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(" /mes", color = Color.White, fontSize = 14.sp)
                }

                Text(plan.dataAmount, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                        Text(text = feature, color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                listOf("@", "f", "🐦", "📷", "in", "📺").forEach {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(it, color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN DE WHATSAPP Y ÍCONO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val message = "Hola, UCB mobile, preciso su ayuda"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/${plan.whatsappNumber}?text=${Uri.encode(message)}")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://web.whatsapp.com/send?phone=${plan.whatsappNumber}&text=${Uri.encode(message)}")
                            }
                            context.startActivity(browserIntent)
                        }
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

                IconButton(
                    onClick = {
                        val message = "Hola, UCB mobile, preciso su ayuda"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/${plan.whatsappNumber}?text=${Uri.encode(message)}")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://web.whatsapp.com/send?phone=${plan.whatsappNumber}&text=${Uri.encode(message)}")
                            }
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF25D366), CircleShape)
                ) {
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
