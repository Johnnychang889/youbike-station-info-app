package com.example.ubikeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ubikeapp.data.Request
import com.example.ubikeapp.data.data
import com.example.ubikeapp.ui.LoginActivity
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        fetchCurrencyData().start()
        findViewById<ConstraintLayout>(R.id.constraintLayout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchCurrencyData(): Thread
    {
        return Thread {
            // 參考資料來源: YouBike2.0臺北市公共自行車即時資訊
            // 網址: https://data.gov.tw/dataset/137993
            val url = URL("https://tcgbusfs.blob.core.windows.net/dotapp/youbike/v2/youbike_immediate.json")
            val connection  = url.openConnection() as HttpsURLConnection

            if(connection.responseCode == 200)
            {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Array<Request>::class.java)
                request.forEach { it.sna = it.sna.substring(11) }
                data.setDataList(request.toList())
                inputStreamReader.close()
                inputSystem.close()
            }
            else
            {
                print("Failed Connection")
            }
        }
    }
}