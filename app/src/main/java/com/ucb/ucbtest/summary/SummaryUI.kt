package com.ucb.ucbtest.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucb.usecases.FinancialRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryUI(viewModel: SummaryViewModel = hiltViewModel(), refreshTrigger: Any? = null) {
    LaunchedEffect(refreshTrigger) {
        viewModel.loadRecords()
    }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen Financiero") }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val currentState = state) {
                is SummaryViewModel.SummaryState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is SummaryViewModel.SummaryState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Resumen de totales
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "Resumen",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Ingresos Totales:")
                                    Text(
                                        "Bs. ${String.format("%.2f", currentState.totalIncome)}",
                                        color = Color.Green
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Gastos Totales:")
                                    Text(
                                        "Bs. ${String.format("%.2f", currentState.totalExpense)}",
                                        color = Color.Red
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Balance:",
                                        fontWeight = FontWeight.Bold
                                    )
                                    val balance = currentState.totalIncome - currentState.totalExpense
                                    Text(
                                        "Bs. ${String.format("%.2f", balance)}",
                                        color = if (balance >= 0) Color.Green else Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Lista de registros
                        if (currentState.records.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay registros financieros")
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                item {
                                    Text(
                                        "Historial de Transacciones",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                                items(currentState.records) { record ->
                                    FinancialRecordItem(record = record)
                                }
                            }
                        }
                    }
                }
                is SummaryViewModel.SummaryState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(currentState.message)
                    }
                }
            }
        }
    }
}

@Composable
fun FinancialRecordItem(record: FinancialRecord) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = try {
        val parsedDate = dateFormat.parse(record.date)
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(parsedDate)
    } catch (e: Exception) {
        record.date
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de tipo (ingreso o gasto)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (record.isExpense) Color.Red.copy(alpha = 0.2f)
                        else Color.Green.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (record.isExpense) "G" else "I",
                    color = if (record.isExpense) Color.Red else Color.Green
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del registro
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = record.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = record.description,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Monto
            Text(
                text = "Bs. ${String.format("%.2f", record.amount)}",
                color = if (record.isExpense) Color.Red else Color.Green,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}