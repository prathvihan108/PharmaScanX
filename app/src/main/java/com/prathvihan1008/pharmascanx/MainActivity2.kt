package com.prathvihan1008.pharmascanx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Show the back button in the toolbar
        //Hello
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val wordList = intent.getStringArrayListExtra("wordList")

        // Convert the list of words to a single string
        val textToShow = wordList?.joinToString(" ")

        // Find the TextView in the layout
        val textView1 = findViewById<TextView>(R.id.textview1)

        // Set the text to the TextView
        textView1.text = textToShow

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