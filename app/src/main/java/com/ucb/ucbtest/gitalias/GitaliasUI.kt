package com.ucb.ucbtest.gitalias

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ucb.ucbtest.R

@Composable
fun GitaliasUI(viewModel: GitaliasViewModel = hiltViewModel()) {
    var alias by remember { mutableStateOf("") }

    val gitaliasState by viewModel.flow.collectAsState()
    val loginState by viewModel.loginflow.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column( horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome: " + loginState
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = alias,
                onValueChange = {
                    alias = it
                },
                label = {
                    Text(stringResource(id = R.string.gitalias_input))
                }
            )
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.fetchGitalias(alias)
                }
            ) {
                Text(stringResource(id = R.string.gitalias_btn_find))
            }
            when( val state = gitaliasState) {
                is GitaliasViewModel.GitaliasState.Init -> {
                    Text("Not image yet")
                }
                is GitaliasViewModel.GitaliasState.Successful -> {
                    Text("${state.model.login}")
                    Log.d("calyr","${state.model.avatarUrl}" )
                    AsyncImage(
                        model = state.model.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp), // Asegura un tamaño fijo
                        contentScale = ContentScale.Crop,
                    )
                }
                is GitaliasViewModel.GitaliasState.Error -> {
                    Text(state.message)
                }
            }
        }
    }
}