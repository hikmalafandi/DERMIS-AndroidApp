package com.capstone.dermis.view.main

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.capstone.dermis.R
import com.capstone.dermis.databinding.ActivityMainBinding
import com.capstone.dermis.rotateFile
import com.capstone.dermis.uriToFile
import com.capstone.dermis.view.camera.CameraActivity
import com.capstone.dermis.view.output.OutputActivity
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import java.io.File
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private var getFile: File? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var skinEvalViewModel: SkinEvalViewModel
    private lateinit var skinDiagViewModel: SkinDiagViewModel

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this, "Don't have permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        skinEvalViewModel = ViewModelProvider(this).get(SkinEvalViewModel::class.java)
        skinDiagViewModel = ViewModelProvider(this).get(SkinDiagViewModel::class.java)


        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnKamera.setOnClickListener { startCamera() }

        binding.btnGalleri.setOnClickListener { startGallery() }

        binding.btnDeteksi.setOnClickListener { skinDetection() }

    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Select photo")
        launcherIntentGallery.launch(chooser)
    }

    private fun skinDetection() {

        if (getFile != null) {

            skinEvalViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            val file = getFile as File

            try {
                skinEvalViewModel.skinEval(file)

                skinEvalViewModel.skinEvaluationResult.observe(this) { result ->
                    skinDiagViewModel.skinDiag(result.condition)

                    skinDiagViewModel.skinDiagnosisResult.observe(this) { diagnosisResult ->
                        val intent = Intent(this@MainActivity, OutputActivity::class.java)
                        intent.putExtra(OutputActivity.EXTRA_IMAGE_URI, getFile?.toUri())
                        intent.putExtra(OutputActivity.EXTRA_CONDITION, result.condition)
                        intent.putExtra(OutputActivity.EXTRA_CONFIDENCE, result.confidence)
                        intent.putExtra(OutputActivity.EXTRA_CAUSE, diagnosisResult.causes)
                        intent.putExtra(OutputActivity.EXTRA_DESC, diagnosisResult.description)
                        intent.putExtra(OutputActivity.EXTRA_TREATMENT, diagnosisResult.treatments)
                        intent.putExtra(OutputActivity.EXTRA_SEVERITY, diagnosisResult.severity.value)
                        startActivity(intent)
                    }
                }

            } catch (e: ServerException) {
                Toast.makeText(this@MainActivity, "An error occurred:: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: ClientException) {
                Toast.makeText(this@MainActivity, "An error occurred:: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: SocketTimeoutException) {
                Toast.makeText(this@MainActivity, "Connection timeout occurred. Try again", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this@MainActivity, R.string.perintah, Toast.LENGTH_SHORT).show()
        }


    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.picture.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@MainActivity)
                getFile = myFile
                binding.picture.setImageURI(uri)
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



}