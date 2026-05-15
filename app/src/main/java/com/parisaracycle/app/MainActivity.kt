package com.parisaracycle.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parisaracycle.app.api.RetrofitClient
import com.parisaracycle.app.models.DangerZone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var webMap: WebView

    private lateinit var tvDangerZones: TextView
    private lateinit var tvEcoStats: TextView
    private lateinit var tvBuddy: TextView

    @SuppressLint("SetJavaScriptEnabled")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Views
        webMap = findViewById(R.id.webMap)

        tvDangerZones =
            findViewById(R.id.tvDangerZones)

        tvEcoStats =
            findViewById(R.id.tvEcoStats)

        tvBuddy =
            findViewById(R.id.tvBuddy)

        val btnLoadMap =
            findViewById<Button>(R.id.btnLoadMap)

        val btnLoadDanger =
            findViewById(R.id.btnLoadDanger)

        // WebView Setup
        webMap.settings.javaScriptEnabled = true

        webMap.webViewClient = WebViewClient()

        // Load Default Map
        webMap.loadUrl(
            "https://www.openstreetmap.org"
        )

        // Map Button
        btnLoadMap.setOnClickListener {

            webMap.loadUrl(
                "https://www.openstreetmap.org/#map=15/12.9716/77.5946"
            )

            Toast.makeText(
                this,
                "Safe Route Loaded",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Load Danger Zones
        btnLoadDanger.setOnClickListener {

            fetchDangerZones()
        }

        // Fake Eco Stats
        val ecoSaved =
            Random.nextInt(300, 1500)

        tvEcoStats.text =
            "🌱 You saved $ecoSaved g CO2 today"

        // Fake Buddy System
        val buddies =
            Random.nextInt(1, 6)

        tvBuddy.text =
            "👥 $buddies cyclists nearby"
    }

    // ----------------------------------------
    // FETCH DANGER ZONES
    // ----------------------------------------

    private fun fetchDangerZones() {

        RetrofitClient.api.getDangerZones()
            .enqueue(object : Callback<List<DangerZone>> {

                override fun onResponse(
                    call: Call<List<DangerZone>>,
                    response: Response<List<DangerZone>>
                ) {

                    if (response.isSuccessful) {

                        val zones = response.body()

                        if (zones != null) {

                            var output = ""

                            for (zone in zones) {

                                output +=
                                    """
⚠️ ${zone.title}

🛣 ${zone.issue_type}
🔥 Severity: ${zone.severity}

📍 ${zone.latitude}, ${zone.longitude}

${zone.description}


                                    """.trimIndent()
                            }

                            tvDangerZones.text = output

                            Log.d(
                                "API_SUCCESS",
                                zones.toString()
                            )
                        }

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Failed To Load Zones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<DangerZone>>,
                    t: Throwable
                ) {

                    Log.e(
                        "API_ERROR",
                        t.message.toString()
                    )

                    Toast.makeText(
                        this@MainActivity,
                        "Backend Connection Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}