# DistrictDriverManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllAgentDriverListUsingGET**](DistrictDriverManagementControllerApi.md#getAllAgentDriverListUsingGET) | **GET** /districtdriver/districtdriver_management/getAllAgentDriverList | getAllAgentDriverList
[**retrieveUsingGET**](DistrictDriverManagementControllerApi.md#retrieveUsingGET) | **GET** /districtdriver/districtdriver_management/retrieve | retrieve
[**updateUsingPOST2**](DistrictDriverManagementControllerApi.md#updateUsingPOST2) | **POST** /districtdriver/districtdriver_management/update | update


<a name="getAllAgentDriverListUsingGET"></a>
# **getAllAgentDriverListUsingGET**
> kotlin.Array&lt;DriverMaster&gt; getAllAgentDriverListUsingGET()

getAllAgentDriverList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DistrictDriverManagementControllerApi()
try {
    val result : kotlin.Array<DriverMaster> = apiInstance.getAllAgentDriverListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DistrictDriverManagementControllerApi#getAllAgentDriverListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DistrictDriverManagementControllerApi#getAllAgentDriverListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;DriverMaster&gt;**](DriverMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="retrieveUsingGET"></a>
# **retrieveUsingGET**
> kotlin.Array&lt;DistrictDriverMapping&gt; retrieveUsingGET(districtId, driverType, regionId)

retrieve

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DistrictDriverManagementControllerApi()
val districtId : kotlin.Int = 56 // kotlin.Int | districtId
val driverType : kotlin.String = driverType_example // kotlin.String | driverType
val regionId : kotlin.Int = 56 // kotlin.Int | regionId
try {
    val result : kotlin.Array<DistrictDriverMapping> = apiInstance.retrieveUsingGET(districtId, driverType, regionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DistrictDriverManagementControllerApi#retrieveUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DistrictDriverManagementControllerApi#retrieveUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **districtId** | **kotlin.Int**| districtId | [optional]
 **driverType** | **kotlin.String**| driverType | [optional]
 **regionId** | **kotlin.Int**| regionId | [optional]

### Return type

[**kotlin.Array&lt;DistrictDriverMapping&gt;**](DistrictDriverMapping.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST2"></a>
# **updateUsingPOST2**
> kotlin.Array&lt;DistrictDriverMapping&gt; updateUsingPOST2(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DistrictDriverManagementControllerApi()
val model : DistrictDriverManagementModelWrapper =  // DistrictDriverManagementModelWrapper | model
try {
    val result : kotlin.Array<DistrictDriverMapping> = apiInstance.updateUsingPOST2(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DistrictDriverManagementControllerApi#updateUsingPOST2")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DistrictDriverManagementControllerApi#updateUsingPOST2")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**DistrictDriverManagementModelWrapper**](DistrictDriverManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;DistrictDriverMapping&gt;**](DistrictDriverMapping.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

