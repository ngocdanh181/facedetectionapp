package com.example.facedetectionapp.data

data class FaceDetectionResult(
    val faceNumber: Int,
    val smilingProbability: Float?,
    val leftEyeOpenProbability: Float?,
    val rightEyeOpenProbability: Float?
)
