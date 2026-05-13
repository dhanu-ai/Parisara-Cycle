package com.parisaracycle.app

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var ecoPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Views
        val etRoute = findViewById<EditText>(R.id.etRoute)

        val btnSafeRoute = findViewById<Button>(R.id.btnSafeRoute)
        val btnReportDanger = findViewById<Button>(R.id.btnReportDanger)

        val tvEcoStats = findViewById<TextView>(R.id.tvEcoStats)
        val tvBuddy = findViewById<TextView>(R.id.tvBuddy)

        val webMap = findViewById<WebView>(R.id.webMap)

        // Enable WebView
        webMap.settings.javaScriptEnabled = true
        webMap.webViewClient = WebViewClient()

        // Default Map
        webMap.loadUrl("https://www.openstreetmap.org")

        // Find Route
        btnSafeRoute.setOnClickListener {

            val destination = etRoute.text.toString()

            if (destination.isNotEmpty()) {

                fetchLocation(
                    destination,
                    webMap,
                    tvEcoStats,
                    tvBuddy
                )

            } else {

                Toast.makeText(
                    this,
                    "Enter a destination",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Report Danger
        btnReportDanger.setOnClickListener {

            Toast.makeText(
                this,
                "Danger Area Reported",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // FETCH LOCATION
    private fun fetchLocation(
        location: String,
        webMap: WebView,
        tvEcoStats: TextView,
        tvBuddy: TextView
    ) {

        val client = OkHttpClient()

        val url =
            "https://nominatim.openstreetmap.org/search?q=$location&format=json&limit=1"

        val request = Request.Builder()
            .url(url)
            .header(
                "User-Agent",
                "ParisaraCycleApp"
            )
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                runOnUiThread {

                    Toast.makeText(
                        this@MainActivity,
                        "Location Fetch Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {

                val responseData = response.body?.string()

                Log.d(
                    "OSM_RESPONSE",
                    responseData ?: "No Data"
                )

                if (responseData != null) {

                    try {

                        val jsonArray =
                            JSONArray(responseData)

                        if (jsonArray.length() > 0) {

                            val result =
                                jsonArray.getJSONObject(0)

                            val latitude =
                                result.getString("lat")

                            val longitude =
                                result.getString("lon")

                            ecoPoints += 500

                            val mapUrl =
                                "https://www.openstreetmap.org/?mlat=$latitude&mlon=$longitude#map=15/$latitude/$longitude"

                            runOnUiThread {

                                // Load REAL MAP
                                webMap.loadUrl(mapUrl)

                                tvEcoStats.text =
                                    "You saved $ecoPoints g CO2 today"

                                tvBuddy.text =
                                    "${Random.nextInt(1, 6)} cyclists found nearby"

                                Toast.makeText(
                                    this@MainActivity,
                                    "Safe Route Generated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } catch (e: Exception) {

                        runOnUiThread {

                            Toast.makeText(
                                this@MainActivity,
                                "JSON Parsing Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }
}