package com.example.demowifi.activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demowifi.R
import com.example.demowifi.adapter.WifiAdapter
import com.example.demowifi.databinding.ActivityMainBinding
import com.example.demowifi.model.Axis

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {
    private lateinit var wifiNetworkAdapter: WifiAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var wifiManager: WifiManager
    private var resultList = ArrayList<ScanResult>()

    private val broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            resultList = wifiManager.scanResults as ArrayList<ScanResult>
            Log.d("TESTING", "onReceive Called")
            stopScanning()

            binding.rvList.layoutManager = LinearLayoutManager(this@MainActivity)
            wifiNetworkAdapter = WifiAdapter(this@MainActivity, resultList)
            binding.rvList.adapter = wifiNetworkAdapter
            wifiNetworkAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

        // Call startScanning() when you want to start the scan, for example, on a button click
        binding.btnScan.setOnClickListener {

            if(wifiManager.isWifiEnabled){
                wifiManager.isWifiEnabled = true
                startScanning()
                Toast.makeText(this, "Result of scanning", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Wifi off please turn on Wifi", Toast.LENGTH_SHORT).show()
                resultList.clear()
                binding.rvList.layoutManager = LinearLayoutManager(this@MainActivity)
                wifiNetworkAdapter = WifiAdapter(this@MainActivity, resultList)
                binding.rvList.adapter = wifiNetworkAdapter
                wifiNetworkAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun startScanning() {
        registerReceiver(
            broadcastReceiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        )
        // Start the Wi-Fi scan
        wifiManager.startScan()

    }

    private fun stopScanning() {
        // Unregister the broadcast receiver
        unregisterReceiver(broadcastReceiver)

        // Process the scan results
        val axisList = ArrayList<Axis>()
        for (result in resultList) {
            Toast.makeText(this@MainActivity, "Wifi : ${result.BSSID}", Toast.LENGTH_SHORT)
            axisList.add(Axis(result.venueName as String, result.operatorFriendlyName))
        }
        Log.d("TESTING", axisList.toString())
    }
}
