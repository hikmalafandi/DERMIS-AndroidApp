package com.capstone.dermis.view.main

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.SkinApi
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.SkinEvaluationResult
import java.io.File

class SkinEvalViewModel : ViewModel() {
    private val _skinEvaluationResult = MutableLiveData<SkinEvaluationResult>()
    val skinEvaluationResult: LiveData<SkinEvaluationResult> get() = _skinEvaluationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun skinEval(file: File) {
        _isLoading.value = (true)
        viewModelScope.launch {
            val fileBytes = withContext(Dispatchers.IO) {file.readBytes()}
            val base64EncodeString = Base64.encodeToString(fileBytes, Base64.DEFAULT)
            val apiInstance = SkinApi()

            try {
                val result : SkinEvaluationResult = withContext(Dispatchers.IO) {
                    apiInstance.imgEval(base64EncodeString)
                }
                _isLoading.value = (false)
                _skinEvaluationResult.value = result
            } catch (e : ServerException) {
                Log.e(TAG, "Error: ${e.message}")
                _isLoading.value = (false)
            } catch (e : ClientException) {
                Log.e(TAG, "Error: ${e.message}")
                _isLoading.value = (false)
            }
        }
    }

    companion object {
        private const val TAG = "SkinEvalViewModel"
    }
}