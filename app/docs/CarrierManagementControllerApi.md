# CarrierManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteCheckUsingGET**](CarrierManagementControllerApi.md#deleteCheckUsingGET) | **GET** /carriermanagement/deleteCheck | deleteCheck
[**getAllCarrierDeliverBetweenTwoDistrictUsingGET**](CarrierManagementControllerApi.md#getAllCarrierDeliverBetweenTwoDistrictUsingGET) | **GET** /carriermanagement/getAllCarrierDeliverBetweenTwoDistrict | getAllCarrierDeliverBetweenTwoDistrict
[**getCarrierDetailsUsingGET**](CarrierManagementControllerApi.md#getCarrierDetailsUsingGET) | **GET** /carriermanagement/getCarrierDetail | getCarrierDetails
[**updateUsingPOST**](CarrierManagementControllerApi.md#updateUsingPOST) | **POST** /carriermanagement/updateCarrierDetail | update


<a name="deleteCheckUsingGET"></a>
# **deleteCheckUsingGET**
> kotlin.Boolean deleteCheckUsingGET(transporterId)

deleteCheck

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CarrierManagementControllerApi()
val transporterId : kotlin.Int = 56 // kotlin.Int | transporterId
try {
    val result : kotlin.Boolean = apiInstance.deleteCheckUsingGET(transporterId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CarrierManagementControllerApi#deleteCheckUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarrierManagementControllerApi#deleteCheckUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **transporterId** | **kotlin.Int**| transporterId |

### Return type

**kotlin.Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAllCarrierDeliverBetweenTwoDistrictUsingGET"></a>
# **getAllCarrierDeliverBetweenTwoDistrictUsingGET**
> kotlin.Array&lt;CarrierModel&gt; getAllCarrierDeliverBetweenTwoDistrictUsingGET(dropDistrictId, dropRegionId, pickupDistrictId, pickupRegionId, totalWeight)

getAllCarrierDeliverBetweenTwoDistrict

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CarrierManagementControllerApi()
val dropDistrictId : kotlin.Int = 56 // kotlin.Int | dropDistrictId
val dropRegionId : kotlin.Int = 56 // kotlin.Int | dropRegionId
val pickupDistrictId : kotlin.Int = 56 // kotlin.Int | pickupDistrictId
val pickupRegionId : kotlin.Int = 56 // kotlin.Int | pickupRegionId
val totalWeight : kotlin.Double = 1.2 // kotlin.Double | totalWeight
try {
    val result : kotlin.Array<CarrierModel> = apiInstance.getAllCarrierDeliverBetweenTwoDistrictUsingGET(dropDistrictId, dropRegionId, pickupDistrictId, pickupRegionId, totalWeight)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CarrierManagementControllerApi#getAllCarrierDeliverBetweenTwoDistrictUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarrierManagementControllerApi#getAllCarrierDeliverBetweenTwoDistrictUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dropDistrictId** | **kotlin.Int**| dropDistrictId |
 **dropRegionId** | **kotlin.Int**| dropRegionId |
 **pickupDistrictId** | **kotlin.Int**| pickupDistrictId |
 **pickupRegionId** | **kotlin.Int**| pickupRegionId |
 **totalWeight** | **kotlin.Double**| totalWeight |

### Return type

[**kotlin.Array&lt;CarrierModel&gt;**](CarrierModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCarrierDetailsUsingGET"></a>
# **getCarrierDetailsUsingGET**
> kotlin.Array&lt;CarrierMaster&gt; getCarrierDetailsUsingGET(category, region)

getCarrierDetails

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CarrierManagementControllerApi()
val category : kotlin.String = category_example // kotlin.String | category
val region : kotlin.String = region_example // kotlin.String | region
try {
    val result : kotlin.Array<CarrierMaster> = apiInstance.getCarrierDetailsUsingGET(category, region)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CarrierManagementControllerApi#getCarrierDetailsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarrierManagementControllerApi#getCarrierDetailsUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **category** | **kotlin.String**| category | [optional]
 **region** | **kotlin.String**| region | [optional]

### Return type

[**kotlin.Array&lt;CarrierMaster&gt;**](CarrierMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST"></a>
# **updateUsingPOST**
> kotlin.Array&lt;CarrierMaster&gt; updateUsingPOST(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CarrierManagementControllerApi()
val model : CarrierManagementModelWrapper =  // CarrierManagementModelWrapper | model
try {
    val result : kotlin.Array<CarrierMaster> = apiInstance.updateUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CarrierManagementControllerApi#updateUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarrierManagementControllerApi#updateUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**CarrierManagementModelWrapper**](CarrierManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;CarrierMaster&gt;**](CarrierMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

