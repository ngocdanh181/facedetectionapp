package com.example.facedetectionapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.facedetectionapp.data.FaceDetectionResult

class ResultDialog(private val results: List<FaceDetectionResult>) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resultdialog, container, false)
        val resultTextView: TextView = view.findViewById(R.id.result_text_view)
        val okButton: Button = view.findViewById(R.id.result_ok_button)

        val resultText = results.joinToString("\n") { result ->
            "FACE NUMBER ${result.faceNumber}:\n" +
                    "Smile: ${result.smilingProbability ?: 0.0 * 100}%\n" +
                    "Left Eye: ${result.leftEyeOpenProbability ?: 0.0 * 100}%\n" +
                    "Right Eye: ${result.rightEyeOpenProbability ?: 0.0 * 100}%"
        }

        resultTextView.text = resultText

        okButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}
