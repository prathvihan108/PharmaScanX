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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_SELECT_IMAGE = 2

    private var editText: EditText? = null
    private lateinit var selectedImageView: ImageView
    private lateinit var selectedImageBitmap: Bitmap
    private var flagImageLoaded=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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

            flagImageLoaded=1

        }
    }

    fun recognizeTextFromImage(bitmap: Bitmap, callback: (Text) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->

                callback(visionText)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()

            }
    }




    fun sendData(view: View) {

        if(flagImageLoaded==1 && editText?.text.toString().trim().isEmpty()) {
            recognizeTextFromImage(selectedImageBitmap) { text ->
                val wordList = mutableListOf<String>()

                // Extract individual words from the recognized text
                for (block in text.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            wordList.add(element.text)
                        }
                    }
                }

                // Pass the list of words to the next activity
                val intent = Intent(this, MainActivity2::class.java)
                intent.putStringArrayListExtra("wordList", ArrayList(wordList))
                startActivity(intent)
            }
        }

        else if(!editText?.text.toString().trim().isEmpty() && flagImageLoaded==0){
            // Get the text from the EditText
            val editTextContent = editText?.text.toString().trim()
// Create a string array with a single element
            val textArray = arrayOf(editTextContent)

// Convert the string array to an ArrayList
            val arrayList = ArrayList(textArray.asList())

// Start the next activity and pass the ArrayList as an extra
            val intent = Intent(this, MainActivity2::class.java)
            intent.putStringArrayListExtra("wordList", arrayList)
            startActivity(intent)
        }
        else if(!editText?.text.toString().trim().isEmpty() && flagImageLoaded==1) {
            Toast.makeText(this,"please choose only one input ",Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this,"please select the picture or enter the drug name",Toast.LENGTH_LONG).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
