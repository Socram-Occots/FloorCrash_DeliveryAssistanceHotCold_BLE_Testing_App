package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.Bundle
import android.widget.Toast

class PeripheralActivity : Activity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        startAdvertising()
    }

    private fun startAdvertising() {
        Toast.makeText(this, "Starting BLE Advertising...", Toast.LENGTH_SHORT).show()
        // Implement BLE advertising logic here
    }
}
