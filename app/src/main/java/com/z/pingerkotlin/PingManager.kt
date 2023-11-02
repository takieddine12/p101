package com.z.pingerkotlin

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader

class PingManager {
    private var pingCount = 0
    private var pings = 0f
    private var isNotDone = true
    private var pingTxt = ""
    private val pingHandler = Handler(Looper.getMainLooper())
    private lateinit var pingView : TextView
    private lateinit var context: Context

    fun startPinging(url: String , pingView : TextView,context: Context) {
        pingCount = 0
        pings = 0f
        isNotDone = true
        pingTxt = ""
        this.pingView = pingView
        this.context = context
        pingSite(url)
    }

    private fun pingSite(url: String) {
        pingCount++
        if (pingCount <= 10 && isNotDone) {
            try {
                val process = Runtime.getRuntime().exec("ping -c 1 $url") // Send a single ping
                val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    if (line!!.contains("time=")) {
                        val startIndex = line!!.indexOf("time=") + 5
                        val endIndex = line!!.indexOf(" ms")
                        val pingTime = line!!.substring(startIndex, endIndex)
                        pings += pingTime.toFloat()
                        Toast.makeText(context, "Count $pingTime",Toast.LENGTH_SHORT).show()
                    }
                }
                if (pingCount >= 10) {
                    isNotDone = false
                    val avgPing = (pings / 10).toString().substringBefore(".")
                    pingTxt = "Average Ping is $avgPing Status is: $isNotDone"
                    pingView.text = pingTxt
                    pingCount = 0
                    pings = 0f
                    // Notify when done, e.g., update UI or perform other actions.
                } else {
                    // Schedule the next ping in 5 seconds.
                    pingHandler.postDelayed({ pingSite(url) }, 200)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun clearHandler(){
       pingHandler.removeCallbacksAndMessages(null)
    }
}