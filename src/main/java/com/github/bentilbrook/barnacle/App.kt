package com.github.bentilbrook.barnacle

import androidx.compose.Composable
import androidx.ui.material.Scaffold
import androidx.ui.tooling.preview.Preview

@Composable
fun App() {
    Scaffold(
        bottomBar = { TabBar() },
        bodyContent = {

        }
    )
}

@Preview
@Composable
fun PreviewApp() {
    App()
}
