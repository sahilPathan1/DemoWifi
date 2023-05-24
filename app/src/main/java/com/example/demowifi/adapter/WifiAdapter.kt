package com.example.demowifi.adapter

import android.content.Context
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demowifi.R
import com.example.demowifi.databinding.SimpleListBinding
import kotlin.math.pow


class WifiAdapter(var context: Context, var wifiNetworks: ArrayList<ScanResult>) :
    RecyclerView.Adapter<WifiAdapter.ViewHolder>() {

    class ViewHolder(var binding: SimpleListBinding) : RecyclerView.ViewHolder(binding.root) {
        val networkNameTextView: TextView = itemView.findViewById(R.id.networkNameTextView)
        val networkNameTextView2: TextView = itemView.findViewById(R.id.networkNameTextView2)

        fun bind(scanResult: android.net.wifi.ScanResult) {
            with(binding) {
                this.result = scanResult

                val distance = calculateDistance(scanResult)

                networkNameTextView2.text = distance.toString()

                val signal = when (distance) {
                    in 0.0..2.0 -> R.drawable.full_wifi
                    in 2.0..4.0 -> R.drawable.law_wifi
                    in 4.0..8.0 -> R.drawable.wifii
                    else -> R.drawable.wifi
                }
                signalLogo.setImageResource(signal)
            }
        }

        private fun calculateDistance(scanResult: android.net.wifi.ScanResult): Double =
            10.0.pow((-69 - scanResult.frequency) / (10.0 * 2.0))

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SimpleListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }

        }

        /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
             val view = LayoutInflater.from(parent.context)
                 .inflate(R.layout.simple_list, parent, false)
             return ViewHolder(view)
         }*/

        /* override fun onBindViewHolder(holder: ViewHolder, position: Int) {
             holder.networkNameTextView.text = wifiNetworks?.get(position)!!.BSSID
             holder.networkNameTextView2.text = wifiNetworks?.get(position)!!.frequency.toString()


         }*/


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return wifiNetworks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = wifiNetworks[position]

        holder.networkNameTextView.text = wifiNetworks?.get(position)!!.BSSID
        holder.networkNameTextView2.text = wifiNetworks?.get(position)!!.frequency.toString()

        holder.bind(currentItem)
    }
}