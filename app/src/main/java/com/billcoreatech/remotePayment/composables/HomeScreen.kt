package com.billcoreatech.remotePayment.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.billcoreatech.remotePayment.R
import com.billcoreatech.remotePayment.composables.destinations.GreetingDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    navigator.navigate(GreetingDestination(stringResource(id = R.string.app_name)), false)
}