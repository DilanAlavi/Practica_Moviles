package com.ucb.ucbtest.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ucb.ucbtest.R

@Composable
fun LoginUI( onSuccess : () ->  Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var context = LocalContext.current
    val viewModel: LoginViewModel = hiltViewModel()

    val loginState by viewModel.loginState.collectAsState(LoginViewModel.LoginState.Init)

    when ( loginState) {
        is LoginViewModel.LoginState.Init -> {
            //Toast.makeText(context, "Init", Toast.LENGTH_LONG).show()
        }
        is LoginViewModel.LoginState.Error -> {
            Toast.makeText(context, (loginState as LoginViewModel.LoginState.Error).message, Toast.LENGTH_LONG).show()
        }
        is LoginViewModel.LoginState.Successful -> {
            //Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
            onSuccess()
        }
        is LoginViewModel.LoginState.Loading -> {
            Toast.makeText(context, "Loading....", Toast.LENGTH_LONG).show()

        }
    }

    Scaffold {
        innerPadding ->
        Box( modifier = Modifier.padding(innerPadding).fillMaxSize().padding(10.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id =  R.drawable.ic_launcher_background),
                    contentDescription = ""

                )
                Text(
                    stringResource(id = R.string.login_title)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    label = {
                        Text("Username")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text("Password")
                    },
                    visualTransformation = PasswordVisualTransformation()

                )
                Box(
                    modifier = Modifier.height(70.dp)
                        .padding(0.dp,10.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            viewModel.doLogin(username, password)
                        }
                    ) { Text("login") }
                }

            }

        }
    }
}