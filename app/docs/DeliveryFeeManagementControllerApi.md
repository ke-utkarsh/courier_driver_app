# DeliveryFeeManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllSenderListUsingGET**](DeliveryFeeManagementControllerApi.md#getAllSenderListUsingGET) | **GET** /deliveryfee/deliveryfeemanagement/getAllSenderList | getAllSenderList
[**getDeliveryFeeDetailsUsingGET**](DeliveryFeeManagementControllerApi.md#getDeliveryFeeDetailsUsingGET) | **GET** /deliveryfee/deliveryfeemanagement/retrieve | getDeliveryFeeDetails
[**updateUsingPOST1**](DeliveryFeeManagementControllerApi.md#updateUsingPOST1) | **POST** /deliveryfee/deliveryfeemanagement/update | update


<a name="getAllSenderListUsingGET"></a>
# **getAllSenderListUsingGET**
> kotlin.Array&lt;SenderModel&gt; getAllSenderListUsingGET()

getAllSenderList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DeliveryFeeManagementControllerApi()
try {
    val result : kotlin.Array<SenderModel> = apiInstance.getAllSenderListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DeliveryFeeManagementControllerApi#getAllSenderListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DeliveryFeeManagementControllerApi#getAllSenderListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;SenderModel&gt;**](SenderModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDeliveryFeeDetailsUsingGET"></a>
# **getDeliveryFeeDetailsUsingGET**
> kotlin.Array&lt;DeliveryFeeRetrieveModel&gt; getDeliveryFeeDetailsUsingGET(districtId, operationType, regionId)

getDeliveryFeeDetails

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DeliveryFeeManagementControllerApi()
val districtId : kotlin.String = districtId_example // kotlin.String | districtId
val operationType : kotlin.Int = 56 // kotlin.Int | operationType
val regionId : kotlin.String = regionId_example // kotlin.String | regionId
try {
    val result : kotlin.Array<DeliveryFeeRetrieveModel> = apiInstance.getDeliveryFeeDetailsUsingGET(districtId, operationType, regionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DeliveryFeeManagementControllerApi#getDeliveryFeeDetailsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DeliveryFeeManagementControllerApi#getDeliveryFeeDetailsUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **districtId** | **kotlin.String**| districtId |
 **operationType** | **kotlin.Int**| operationType |
 **regionId** | **kotlin.String**| regionId |

### Return type

[**kotlin.Array&lt;DeliveryFeeRetrieveModel&gt;**](DeliveryFeeRetrieveModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST1"></a>
# **updateUsingPOST1**
> kotlin.Array&lt;DeliveryFeeRetrieveModel&gt; updateUsingPOST1(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DeliveryFeeManagementControllerApi()
val model : DeliveryFeeManagementModelWrapper =  // DeliveryFeeManagementModelWrapper | model
try {
    val result : kotlin.Array<DeliveryFeeRetrieveModel> = apiInstance.updateUsingPOST1(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DeliveryFeeManagementControllerApi#updateUsingPOST1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DeliveryFeeManagementControllerApi#updateUsingPOST1")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**DeliveryFeeManagementModelWrapper**](DeliveryFeeManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;DeliveryFeeRetrieveModel&gt;**](DeliveryFeeRetrieveModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

