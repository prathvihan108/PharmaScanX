package com.prathvihan1008.pharmascanx

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_SELECT_IMAGE = 2

    private var editText: EditText? = null
    private lateinit var selectedImageView: ImageView
    private lateinit var selectedImageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Show the back button in the toolbar
     //   supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        editText = findViewById(R.id.editText)
        selectedImageView = findViewById(R.id.selectedImageView)
    }

    fun selectImage(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_IMAGE)
    }

    fun takePicture(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                val extras = data.extras
                val imageBitmap = extras?.get("data") as Bitmap
                selectedImageView.setImageBitmap(imageBitmap)
                selectedImageView.visibility = View.VISIBLE
                selectedImageBitmap = imageBitmap
            } else if (requestCode == REQUEST_SELECT_IMAGE && data != null) {
                val selectedImageUri = data.data
                selectedImageView.setImageURI(selectedImageUri)
                selectedImageView.visibility = View.VISIBLE
                // You may need to resize or handle large images to avoid memory issues
                // Also, you can use content resolver to load the image bitmap if needed
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
            }
        }
    }

    fun sendData(view: View) {
        // Use the selectedImageBitmap and editText.text.toString() for further processing
        // For example, you can pass these values to your Google ML OCR for text extraction

        // Create an Intent to start the next activity
        val intent = Intent(this, MainActivity2::class.java)

        // Pass data to the next activity if needed
//        intent.putExtra("imageBitmap", selectedImageBitmap)
//        intent.putExtra("text", editText?.text.toString())

        // Start the next activity
        startActivity(intent)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
