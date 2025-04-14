package com.ucb.ucbtest.takephoto

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TakePhotoViewModel: ViewModel() {
    private val _photo = MutableStateFlow<TakePhotoState>(TakePhotoState.Init)
    val photo : StateFlow<TakePhotoState> = _photo

    sealed class TakePhotoState  {
        object Taking : TakePhotoState()
        object Init : TakePhotoState()
    }

    fun take() {
        _photo.value = TakePhotoState.Taking
    }

}