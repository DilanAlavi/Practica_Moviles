package com.ucb.ucbtest.takephoto

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ucb.ucbtest.R

@Composable
fun TakePhotoUI() {
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val (hasPermission, requestPermission, shouldShowRationale) = RequestCameraPermission()

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            imageBitmap.value = bitmap
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!hasPermission) {
                Text("Se necesita permiso para acceder a la cámara.")
                Spacer(modifier = Modifier.height(6.dp))

                if (shouldShowRationale) {
                    Text("Debes conceder el permiso para poder tomar fotos.")
                }

                Button(onClick = requestPermission) {
                    Text("Conceder permiso")
                }
            } else {
                OutlinedButton(
                    onClick = { cameraLauncher.launch() }
                ) {
                    Text(stringResource(R.string.btn_take_photo))
                }

                Spacer(modifier = Modifier.height(6.dp))

                imageBitmap.value?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto tomada",
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RequestCameraPermission(): Triple<Boolean, () -> Unit, Boolean> {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var shouldShowRationale by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasPermission = isGranted
        shouldShowRationale = !isGranted
        if (!isGranted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    return Triple(hasPermission, { permissionLauncher.launch(Manifest.permission.CAMERA) }, shouldShowRationale)
}
