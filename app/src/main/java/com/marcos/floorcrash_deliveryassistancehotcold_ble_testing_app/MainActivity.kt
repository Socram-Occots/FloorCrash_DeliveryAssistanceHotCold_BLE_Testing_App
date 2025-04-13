package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private var modeBLE : String = "CENTRAL"
    private var isCentral : Boolean = true
    private var webView : WebView? = null
    private var otherdeviceID : String? = null

    @RequiresApi(Build.VERSION_CODES.S)
    private val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                requestBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            } else {
                Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private val requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth Required!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPermissions()) {
            requestBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } else {
            requestPermissions.launch(requiredPermissions)
        }

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            addJavascriptInterface(WebAppInterface(), "AndroidBridge")
            setBackgroundColor(0xFF000000.toInt())
            loadUrl("file:///android_asset/index.html")
            layoutParams = android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        setContent {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { webView!! },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    inner class WebAppInterface {

        @JavascriptInterface
        fun setBLEMode(mode: String) {
            modeBLE = mode
            isCentral = mode == "CENTRAL"
        }

        @JavascriptInterface
        fun receiveID(input: String, mode: String) {
            Toast.makeText(this@MainActivity, "Connecting as $mode to $input", Toast.LENGTH_SHORT).show()
            otherdeviceID = input
        }

        @JavascriptInterface
        fun startBLE() {
            if (isCentral) {
                val intent = Intent(this@MainActivity, CentralActivity::class.java)
                intent.putExtra("TARGET_DEVICE_ID", otherdeviceID)
                startActivity(intent)
            } else {
                val intent = Intent(this@MainActivity, PeripheralActivity::class.java)
                intent.putExtra("TARGET_DEVICE_ID", otherdeviceID)
                startActivity(intent)
            }
        }

        @JavascriptInterface
        fun backToMain() {
            runOnUiThread {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}