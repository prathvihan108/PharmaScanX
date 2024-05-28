package com.prathvihan1008.pharmascanx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.datatransport.Priority
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val wordList = intent.getStringArrayListExtra("wordList")


        val textToShow = wordList?.joinToString(" ")

        // Find the TextView in the layout
        val textView1 = findViewById<TextView>(R.id.textview1)

        // Set the text to the TextView
        textView1.text = textToShow

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.fda.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create the service
        val service = retrofit.create(FDAApiService::class.java)

        // Make the API call
        service.getDrugLabel(textToShow).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    // Log the entire JSON response
                    Log.d("JSON Response", response.body().toString())
                    textView1.text=response.body().toString()

                } else {
                    Log.e("API Call", "Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("API Call", "Failed: ${t.message}")
            }
        })







    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the previous activity
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}