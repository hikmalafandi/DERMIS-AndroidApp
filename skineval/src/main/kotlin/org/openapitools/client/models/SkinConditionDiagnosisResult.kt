/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.openapitools.client.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Skin condition diagnosis result containing the condition's description, causes, treatments, and severity in JSON format
 *
 * @param description
 * @param causes
 * @param treatments
 * @param severity
 */


data class SkinConditionDiagnosisResult (

    @Json(name = "description")
    val description: kotlin.String,

    @Json(name = "causes")
    val causes: kotlin.String,

    @Json(name = "treatments")
    val treatments: kotlin.String,

    @Json(name = "severity")
    val severity: SkinConditionDiagnosisResult.Severity

) {

    /**
     *
     *
     * Values: low,medium,high
     */
    @JsonClass(generateAdapter = false)
    enum class Severity(val value: kotlin.String) {
        @Json(name = "low") low("low"),
        @Json(name = "medium") medium("medium"),
        @Json(name = "high") high("high");
    }
}
