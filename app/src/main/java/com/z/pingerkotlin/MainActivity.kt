package com.z.pingerkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    private var pings = 0f
    private var pingCount = 0
    private var isNotDone = true
    private var pingHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pingSite = findViewById<Button>(R.id.pingSite)
        val getRsrp = findViewById<Button>(R.id.getRsrp)
        val pingView = findViewById<TextView>(R.id.pingTxt)

        getRsrp.setOnClickListener {
            val handler = Handler()
            val runnable = object : java.lang.Runnable {
                override fun run() {
                    getRsrpValues()
                    handler.postDelayed(this,5000)
                }
            }
            handler.postDelayed(runnable,1000)
        }
        pingSite.setOnClickListener {
            PingManager().startPinging("www.google.com",pingView,this)
        }
    }


    // TODO : RSRP VALUES
    @SuppressLint("MissingPermission", "NewApi")
    private fun getRsrpValues(){
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val cellInfoList = telephonyManager.allCellInfo
        for (cellInfo in cellInfoList){
            if(cellInfo is CellInfoLte){
                val rsrp = cellInfo.cellSignalStrength.rsrp
                Toast.makeText(this,"Rsrp $rsrp",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        PingManager().clearHandler()
        super.onDestroy()
    }
}