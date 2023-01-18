package com.billcoreatech.remotePayment.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}