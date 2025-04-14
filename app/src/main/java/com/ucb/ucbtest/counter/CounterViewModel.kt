package com.ucb.ucbtest.counter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ucb.ucbtest.service.Util

class CounterViewModel: ViewModel() {

    private val _cadena = MutableLiveData("0")
    val cadena: LiveData<String> = _cadena

    fun increment(context: Context) {
        _cadena.value = ((_cadena.value?.toIntOrNull() ?:0) + 1).toString()
        Util.sendNotificatión(context)
    }

}