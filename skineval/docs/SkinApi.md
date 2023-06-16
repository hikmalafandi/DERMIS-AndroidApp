# SkinApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**imgEval**](SkinApi.md#imgEval) | **POST** /imgeval | Evaluate skin condition based on the given image


<a id="imgEval"></a>
# **imgEval**
> SkinEvaluationResult imgEval(body)

Evaluate skin condition based on the given image

Predict skin condition based on the image in request body with trained intelligent model. Returns possible condition and its confidence level. 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = SkinApi()
val body : kotlin.ByteArray = BYTE_ARRAY_DATA_HERE // kotlin.ByteArray | Contains data used for prediction
try {
    val result : SkinEvaluationResult = apiInstance.imgEval(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SkinApi#imgEval")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SkinApi#imgEval")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | **kotlin.ByteArray**| Contains data used for prediction |

### Return type

[**SkinEvaluationResult**](SkinEvaluationResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

