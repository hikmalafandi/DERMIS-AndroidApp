package com.capstone.dermis.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.DiagnosisApi
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.SkinConditionDiagnosisResult

class SkinDiagViewModel : ViewModel() {

    private val _skinDiagnosisResult = MutableLiveData<SkinConditionDiagnosisResult>()
    val skinDiagnosisResult: LiveData<SkinConditionDiagnosisResult> get() = _skinDiagnosisResult


    fun skinDiag(condition : String) {
        viewModelScope.launch {
            val apiInstance = DiagnosisApi()

            try {
                val result: SkinConditionDiagnosisResult = withContext(Dispatchers.IO) {apiInstance.getDiseaseInfo(condition)}
                _skinDiagnosisResult.value = result
            } catch (e: ServerException) {
                Log.e(TAG, "Error: ${e.message}")
            } catch (e: ClientException) {
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "skinDiagViewModel"
    }

}