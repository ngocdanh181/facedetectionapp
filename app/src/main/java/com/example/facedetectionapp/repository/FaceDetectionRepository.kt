package com.example.facedetectionapp.repository

import android.graphics.Bitmap
import com.example.facedetectionapp.data.FaceDetectionResult
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import javax.inject.Inject

class FaceDetectionRepository @Inject constructor() {
    fun detectFaces(imageBitmap: Bitmap, onSuccess: (List<FaceDetectionResult>) -> Unit) {
        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap)
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

        detector.detectInImage(firebaseVisionImage)
            .addOnSuccessListener { faces ->
                val results = faces.mapIndexed { index, face ->
                    FaceDetectionResult(
                        faceNumber = index + 1,
                        smilingProbability = face.smilingProbability,
                        leftEyeOpenProbability = face.leftEyeOpenProbability,
                        rightEyeOpenProbability = face.rightEyeOpenProbability
                    )
                }
                onSuccess(results)
            }
    }
}