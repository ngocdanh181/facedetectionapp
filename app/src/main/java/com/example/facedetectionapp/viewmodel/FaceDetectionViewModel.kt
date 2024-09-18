package com.example.facedetectionapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.facedetectionapp.data.FaceDetectionResult
import com.example.facedetectionapp.repository.FaceDetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel@Inject constructor(
    private val repository: FaceDetectionRepository
) : ViewModel()  {
    private val _detectionResults = MutableLiveData<List<FaceDetectionResult>>()
    val detectionResults: LiveData<List<FaceDetectionResult>> get() = _detectionResults

    fun detectFaces(imageBitmap: Bitmap) {
        repository.detectFaces(imageBitmap) { results ->
            _detectionResults.postValue(results)
        }
    }

}
