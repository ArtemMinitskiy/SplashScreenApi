package com.example.splashscreenapi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private val _splashFakeApiResponse = MutableStateFlow(false)
    val splashFakeApiResponse = _splashFakeApiResponse.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            delay(3000)
            _splashFakeApiResponse.value = true
        }
    }
}