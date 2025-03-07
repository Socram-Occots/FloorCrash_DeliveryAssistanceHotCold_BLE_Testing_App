package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.viewinterop.AndroidView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Load the local React app from assets
        webView.loadUrl("file:///android_asset/index.html")

        setContent {
            AndroidView(factory = { webView })
        }
    }
}
