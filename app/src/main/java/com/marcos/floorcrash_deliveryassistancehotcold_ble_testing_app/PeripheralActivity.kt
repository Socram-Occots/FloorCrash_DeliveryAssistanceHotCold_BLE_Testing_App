package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.Manifest
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import java.util.*

class PeripheralActivity : ComponentActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var advertiser: BluetoothLeAdvertiser? = null
    private var advertiseCallback: AdvertiseCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser
        startAdvertising()
    }

    private val pingHandler = Handler(Looper.getMainLooper())
    private val pingRunnable = object : Runnable {
        override fun run() {
            Toast.makeText(applicationContext, "Received ping from central", Toast.LENGTH_SHORT).show()
            pingHandler.postDelayed(this, 5000)
        }
    }


    private fun startAdvertising() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Missing BLE Advertise Permission", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .build()

        advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                pingHandler.post(pingRunnable)
                Toast.makeText(applicationContext, "Advertising started", Toast.LENGTH_SHORT).show()
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                Toast.makeText(applicationContext, "Advertising failed: $errorCode", Toast.LENGTH_SHORT).show()
            }
        }

        advertiser?.startAdvertising(settings, data, advertiseCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
            advertiser?.stopAdvertising(advertiseCallback)
            pingHandler.removeCallbacksAndMessages(null)
        }
    }
}