package com.ucb.ucbtest.counter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterUI() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel : CounterViewModel = viewModel()
    var cadena by remember { mutableStateOf<String>("0") }

    fun updateUI(s: String) {
        cadena = s
    }
    viewModel.cadena.observe(
        lifecycleOwner,
        Observer(::updateUI)
    )
    val context = LocalContext.current
    Scaffold {
        innerPadding ->  Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(cadena)
        Button(
            onClick = {
                viewModel.increment( context =  context)
            }
        ) {
            Text("Increment")
        }
    }
    }


}

@Composable
fun CounterUIV2() {
    val viewModel: CounterViewModel = viewModel()
    val cadena by viewModel.cadena.observeAsState("0")
    val context = LocalContext.current
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("IU V2")
            Text(cadena)
            Button(
                onClick = {
                    viewModel.increment(context = context)
                }
            ) {
                Text("Add")
            }
        }
    }
}
