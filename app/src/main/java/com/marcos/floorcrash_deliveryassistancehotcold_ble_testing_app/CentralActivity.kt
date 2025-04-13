package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

class CentralActivity : ComponentActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothScanner: BluetoothLeScanner
    private lateinit var handler: Handler
    private var otherdeviceID : String? = null
    private val pingHandler = Handler(Looper.getMainLooper())

    private var foundDevice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothScanner = bluetoothAdapter.bluetoothLeScanner
        handler = Handler(Looper.getMainLooper())

        otherdeviceID = intent.getStringExtra("TARGET_DEVICE_ID").toString()
        if (otherdeviceID.isNullOrBlank()) {
            Toast.makeText(this, "Missing target device ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        startScanning()
    }

    private val pingRunnable = object : Runnable {
        override fun run() {
            Toast.makeText(applicationContext, "Pinging peripheral...", Toast.LENGTH_SHORT).show()
            pingHandler.postDelayed(this, 5000)
        }
    }

    private fun checkPerms() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            finish()
        }
    }
    @SuppressLint("MissingPermission")
    private fun initiateScanCheckPerms() {
        checkPerms()
        bluetoothScanner.startScan(scanCallback)
    }

    @SuppressLint("MissingPermission")
    private fun initiateCanStopCheckPerms() {
        checkPerms()
        bluetoothScanner.stopScan(scanCallback)
    }
    @SuppressLint("MissingPermission")
    private fun getDeviceNameCheckPerms(it : ScanResult) : String {
        checkPerms()
        return it.device.name.toString()
    }

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let {
                if (!foundDevice && it.device.address.equals(otherdeviceID, ignoreCase = true)) {
                    foundDevice = true
                    Toast.makeText(applicationContext, "Found target peripheral: ${getDeviceNameCheckPerms(it)}", Toast.LENGTH_SHORT).show()
                    pingHandler.post(pingRunnable)
                }
            }
        }
    }

//    override fun onScanFailed(errorCode: Int) {
//            Toast.makeText(applicationContext, "Scan failed with error: $errorCode", Toast.LENGTH_SHORT).show()
//    }

    private fun startScanning() {
        handler.post(object : Runnable {
            override fun run() {
                foundDevice = false
                initiateScanCheckPerms()
                Toast.makeText(applicationContext, "Scanning...", Toast.LENGTH_SHORT).show()

                handler.postDelayed({
                    initiateCanStopCheckPerms()

                    if (!foundDevice) {
                        Toast.makeText(applicationContext, "No peripherals found.", Toast.LENGTH_SHORT).show()
                    }

                    handler.postDelayed(this, 5000)
                }, 5000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        initiateCanStopCheckPerms()
        handler.removeCallbacksAndMessages(null)
        pingHandler.removeCallbacksAndMessages(null)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
