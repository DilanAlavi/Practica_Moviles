package com.ucb.ucbtest.income

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ucb.domain.Income
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeUI(viewModel: IncomeViewModel = hiltViewModel(), onNewIncome: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var incomeToDelete by remember { mutableStateOf<Income?>(null) }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Mostrar mensaje de éxito o error al guardar
    LaunchedEffect(saveState) {
        if (saveState == true) {
            // Éxito al guardar
            showDialog = false
            name = ""
            amount = ""
            description = ""
            viewModel.resetSaveState()
            onNewIncome()
        }
    }

    // Observar cambios en el estado de eliminación
    LaunchedEffect(deleteState) {
        if (deleteState != null) {
            viewModel.resetDeleteState()
            showDeleteConfirmDialog = false
            incomeToDelete = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Ingresos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar ingreso")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val currentState = state) {
                is IncomeViewModel.IncomeState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is IncomeViewModel.IncomeState.Success -> {
                    if (currentState.incomes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay ingresos registrados")
                        }
                    } else {
                        IncomesList(
                            incomes = currentState.incomes,
                            onDeleteClick = { income ->
                                incomeToDelete = income
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
                is IncomeViewModel.IncomeState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(currentState.message)
                    }
                }
            }
        }
    }

    // Dialog para agregar nuevo ingreso
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nuevo Ingreso") },
            text = {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Monto") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveIncome(name, amount, description)
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog para confirmar eliminación
    if (showDeleteConfirmDialog && incomeToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                incomeToDelete = null
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este ingreso?") },
            confirmButton = {
                Button(
                    onClick = {
                        incomeToDelete?.let {
                            viewModel.deleteIncome(it.id)
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmDialog = false
                        incomeToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun IncomesList(incomes: List<Income>, onDeleteClick: (Income) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(incomes) { income ->
            IncomeItem(income = income, onDeleteClick = onDeleteClick)
        }
    }
}

@Composable
fun IncomeItem(income: Income, onDeleteClick: (Income) -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = try {
        val parsedDate = dateFormat.parse(income.date)
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(parsedDate)
    } catch (e: Exception) {
        income.date
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = income.name,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onDeleteClick(income) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bs. ${String.format("%.2f", income.amount)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = income.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Fecha: $date",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}