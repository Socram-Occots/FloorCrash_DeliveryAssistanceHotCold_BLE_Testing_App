package com.marcos.floorcrash_deliveryassistancehotcold_ble_testing_app

import android.app.Activity
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast

class CentralActivity : Activity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothScanner: BluetoothLeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothScanner = bluetoothAdapter.bluetoothLeScanner

        startScanning()
    }

    private fun startScanning() {
        Toast.makeText(this, "Starting BLE Scan...", Toast.LENGTH_SHORT).show()
        // Implement BLE scanning logic here
    }
}
