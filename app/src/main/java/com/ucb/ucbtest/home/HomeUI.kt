
package com.ucb.ucbtest.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucb.ucbtest.mobileplan.MobilePlanCard
import com.ucb.ucbtest.mobileplan.MobilePlanViewModel

@Composable
fun HomeUI(
    mobileViewModel: MobilePlanViewModel = hiltViewModel(),
    onPlanSelected: (String) -> Unit = {}
) {
    val plans by mobileViewModel.plans.collectAsState()
    val currentPlanIndex by mobileViewModel.currentPlanIndex.collectAsState()

    LaunchedEffect(Unit) {
        mobileViewModel.loadPlans()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (plans.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left arrow button
                    IconButton(
                        onClick = { mobileViewModel.showPreviousPlan() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Plan anterior",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFFFF6B6B)
                        )
                    }

                    // Plan indicator
                    Text(
                        text = "${currentPlanIndex + 1} / ${plans.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF6B6B)
                    )

                    // Right arrow button
                    IconButton(
                        onClick = { mobileViewModel.showNextPlan() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Siguiente plan",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFFFF6B6B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current plan display
                val currentPlan = mobileViewModel.getCurrentPlan()
                currentPlan?.let { plan ->
                    MobilePlanCard(
                        plan = plan,
                        onPlanSelected = onPlanSelected
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFF6B6B))
            }
        }
    }
}