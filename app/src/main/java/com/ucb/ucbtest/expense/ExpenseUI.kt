package com.ucb.ucbtest.expense

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
import com.ucb.domain.Expense
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseUI(viewModel: ExpenseViewModel = hiltViewModel(), onNewExpense: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }
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
            onNewExpense()
        }
    }

    // Observar cambios en el estado de eliminación
    LaunchedEffect(deleteState) {
        if (deleteState != null) {
            viewModel.resetDeleteState()
            showDeleteConfirmDialog = false
            expenseToDelete = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Gastos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar gasto")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val currentState = state) {
                is ExpenseViewModel.ExpenseState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ExpenseViewModel.ExpenseState.Success -> {
                    if (currentState.expenses.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay gastos registrados")
                        }
                    } else {
                        ExpensesList(
                            expenses = currentState.expenses,
                            onDeleteClick = { expense ->
                                expenseToDelete = expense
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
                is ExpenseViewModel.ExpenseState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(currentState.message)
                    }
                }
            }
        }
    }

    // Dialog para agregar nuevo gasto
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Nuevo Gasto") },
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
                        viewModel.saveExpense(name, amount, description)
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
    if (showDeleteConfirmDialog && expenseToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmDialog = false
                expenseToDelete = null
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este gasto?") },
            confirmButton = {
                Button(
                    onClick = {
                        expenseToDelete?.let {
                            viewModel.deleteExpense(it.id)
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
                        expenseToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ExpensesList(expenses: List<Expense>, onDeleteClick: (Expense) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(expenses) { expense ->
            ExpenseItem(expense = expense, onDeleteClick = onDeleteClick)
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDeleteClick: (Expense) -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = try {
        val parsedDate = dateFormat.parse(expense.date)
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(parsedDate)
    } catch (e: Exception) {
        expense.date
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
                    text = expense.name,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { onDeleteClick(expense) }
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
                text = "Bs. ${String.format("%.2f", expense.amount)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = expense.description,
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