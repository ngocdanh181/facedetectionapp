package com.example.facedetectionapp

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.facedetectionapp.databinding.ActivityMainBinding
import com.example.facedetectionapp.viewmodel.FaceDetectionViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: FaceDetectionViewModel by viewModels()
    private lateinit var resultDialog: ResultDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_TEXT_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.buttonTextRecognition.setOnClickListener {

            recognizeText()
        }

        viewModel.detectionResults.observe(this, Observer { results ->
            resultDialog = ResultDialog(results)
            resultDialog.show(supportFragmentManager, "result_dialog")
        })
    }

    private fun recognizeText() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_TEXT_CAPTURE)
            }
        }
    }

    private fun recognizeTextImage(bitmap: Bitmap){
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText -> // Task completed successfully
                // Process recognized text here
                val recognizedText = visionText.text
                Log.d("Text Recognition", recognizedText)
                binding.textView.text = recognizedText
            }
            .addOnFailureListener { e -> // Task failed with an exception
                e.printStackTrace()
            }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.detectFaces(imageBitmap)
        }
        if (requestCode == REQUEST_TEXT_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            recognizeTextImage(imageBitmap)
        }
    }
}
