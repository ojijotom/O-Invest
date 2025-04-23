package com.ojijo.o_invest.ui.screens.portfolio

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.rememberNavController

@Composable
fun PortfolioScreen(navController: NavController) {
    // Get context for WebView
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Portfolio Screen",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Embed WebView in Compose using AndroidView
        WebViewComposable(context)
    }
}

@Composable
fun WebViewComposable(context: android.content.Context) {
    // Use AndroidView to display a traditional WebView inside Compose
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // Keeps navigation inside the WebView
                settings.javaScriptEnabled = true // Enable JavaScript
                loadUrl("file:///android_asset/portfolio.html") // Load your HTML from assets
            }
        },
        modifier = Modifier.fillMaxSize() // Ensure WebView fills the available space
    )
}

@Preview(showBackground = true)
@Composable
fun PortfolioScreenPreview() {
    PortfolioScreen(navController = rememberNavController()) // Preview with dummy navController
}