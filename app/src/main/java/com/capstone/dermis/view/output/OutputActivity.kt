package com.capstone.dermis.view.output

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.capstone.dermis.R
import com.capstone.dermis.databinding.ActivityOutputBinding
import java.math.BigDecimal

class OutputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutputBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri = intent.getParcelableExtra(EXTRA_IMAGE_URI)
        val condition = intent.getStringExtra(EXTRA_CONDITION)
        val confidence = intent.getSerializableExtra(EXTRA_CONFIDENCE) as BigDecimal


        val cause = intent.getStringExtra(EXTRA_CAUSE)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val treatment = intent.getStringExtra(EXTRA_TREATMENT)
        val severity = intent.getStringExtra(EXTRA_SEVERITY)


        binding.outputCondition.text = condition
        binding.outputConfidence.text = confidence.toString().substring(0, 4)
        binding.outputCause.text = cause
        binding.outputDesc.text = desc
        binding.outputTreatment.text = treatment
        binding.outputSeverity.text = severity


        val minConfidence = BigDecimal(0.4)

        if (severity == "high" || severity == "medium" || confidence < minConfidence) {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.alert_recommendation)
                setMessage(R.string.alert_recommendation_message)
                setPositiveButton("OK") {dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }

        loadImageFromUri()
    }


    companion object {
        const val EXTRA_CONDITION = "extra_condition"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_CAUSE = "extra_cause"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_TREATMENT = "extra_treatment"
        const val EXTRA_SEVERITY = "extra_severity"
    }

    private fun loadImageFromUri() {
        imageUri?.let { uri ->
            binding.picture.setImageURI(uri)
        }
    }

}

