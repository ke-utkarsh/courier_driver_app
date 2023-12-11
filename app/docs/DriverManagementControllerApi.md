# DriverManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAgentDriverListWithWeightOnADayUsingGET**](DriverManagementControllerApi.md#getAgentDriverListWithWeightOnADayUsingGET) | **GET** /driver/drivermanagement/getAgentDriverListWithWeightOnADay | getAgentDriverListWithWeightOnADay
[**getAssignedDriverUsingGET**](DriverManagementControllerApi.md#getAssignedDriverUsingGET) | **GET** /driver/drivermanagement/getAssignedOrdersToDriver | getAssignedDriver
[**getDriverDeliveryFeeUsingGET**](DriverManagementControllerApi.md#getDriverDeliveryFeeUsingGET) | **GET** /driver/drivermanagement/getDriverDeliveryFee | getDriverDeliveryFee
[**getDriverListWithMorningEveningWeightUsingGET**](DriverManagementControllerApi.md#getDriverListWithMorningEveningWeightUsingGET) | **GET** /driver/drivermanagement/getDriverListWithMorningEveningWeight | getDriverListWithMorningEveningWeight
[**getDriverListWithWeightOnADayUsingGET**](DriverManagementControllerApi.md#getDriverListWithWeightOnADayUsingGET) | **GET** /driver/drivermanagement/getDriverListWithWeightOnADay | getDriverListWithWeightOnADay
[**retrieveUsingGET1**](DriverManagementControllerApi.md#retrieveUsingGET1) | **GET** /driver/drivermanagement/retrieve | retrieve
[**updateUsingPOST3**](DriverManagementControllerApi.md#updateUsingPOST3) | **POST** /driver/drivermanagement/update | update


<a name="getAgentDriverListWithWeightOnADayUsingGET"></a>
# **getAgentDriverListWithWeightOnADayUsingGET**
> kotlin.Array&lt;CarrierModel&gt; getAgentDriverListWithWeightOnADayUsingGET(dropDistrictId, dropRegionId, itemWeight, orderType, pickipDistrictId, pickupRegionId, pickUpDate, slot)

getAgentDriverListWithWeightOnADay

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val dropDistrictId : kotlin.Int = 56 // kotlin.Int | dropDistrictId
val dropRegionId : kotlin.Int = 56 // kotlin.Int | dropRegionId
val itemWeight : kotlin.Double = 1.2 // kotlin.Double | itemWeight
val orderType : kotlin.Int = 56 // kotlin.Int | orderType
val pickipDistrictId : kotlin.Int = 56 // kotlin.Int | pickipDistrictId
val pickupRegionId : kotlin.Int = 56 // kotlin.Int | pickupRegionId
val pickUpDate : kotlin.String = pickUpDate_example // kotlin.String | pickUpDate
val slot : kotlin.String = slot_example // kotlin.String | slot
try {
    val result : kotlin.Array<CarrierModel> = apiInstance.getAgentDriverListWithWeightOnADayUsingGET(dropDistrictId, dropRegionId, itemWeight, orderType, pickipDistrictId, pickupRegionId, pickUpDate, slot)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#getAgentDriverListWithWeightOnADayUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#getAgentDriverListWithWeightOnADayUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dropDistrictId** | **kotlin.Int**| dropDistrictId |
 **dropRegionId** | **kotlin.Int**| dropRegionId |
 **itemWeight** | **kotlin.Double**| itemWeight |
 **orderType** | **kotlin.Int**| orderType |
 **pickipDistrictId** | **kotlin.Int**| pickipDistrictId |
 **pickupRegionId** | **kotlin.Int**| pickupRegionId |
 **pickUpDate** | **kotlin.String**| pickUpDate | [optional]
 **slot** | **kotlin.String**| slot | [optional]

### Return type

[**kotlin.Array&lt;CarrierModel&gt;**](CarrierModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAssignedDriverUsingGET"></a>
# **getAssignedDriverUsingGET**
> kotlin.Array&lt;Order&gt; getAssignedDriverUsingGET(driverId)

getAssignedDriver

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val driverId : kotlin.Int = 56 // kotlin.Int | driverId
try {
    val result : kotlin.Array<Order> = apiInstance.getAssignedDriverUsingGET(driverId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#getAssignedDriverUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#getAssignedDriverUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **driverId** | **kotlin.Int**| driverId |

### Return type

[**kotlin.Array&lt;Order&gt;**](Order.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDriverDeliveryFeeUsingGET"></a>
# **getDriverDeliveryFeeUsingGET**
> kotlin.Array&lt;AgentDeliveryFeeMaster&gt; getDriverDeliveryFeeUsingGET(driverId)

getDriverDeliveryFee

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val driverId : kotlin.Int = 56 // kotlin.Int | driverId
try {
    val result : kotlin.Array<AgentDeliveryFeeMaster> = apiInstance.getDriverDeliveryFeeUsingGET(driverId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#getDriverDeliveryFeeUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#getDriverDeliveryFeeUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **driverId** | **kotlin.Int**| driverId |

### Return type

[**kotlin.Array&lt;AgentDeliveryFeeMaster&gt;**](AgentDeliveryFeeMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDriverListWithMorningEveningWeightUsingGET"></a>
# **getDriverListWithMorningEveningWeightUsingGET**
> kotlin.Array&lt;CarrierModel&gt; getDriverListWithMorningEveningWeightUsingGET(pickUpDate, timezoneOffset)

getDriverListWithMorningEveningWeight

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val pickUpDate : kotlin.String = pickUpDate_example // kotlin.String | pickUpDate
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : kotlin.Array<CarrierModel> = apiInstance.getDriverListWithMorningEveningWeightUsingGET(pickUpDate, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#getDriverListWithMorningEveningWeightUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#getDriverListWithMorningEveningWeightUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pickUpDate** | **kotlin.String**| pickUpDate |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |

### Return type

[**kotlin.Array&lt;CarrierModel&gt;**](CarrierModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDriverListWithWeightOnADayUsingGET"></a>
# **getDriverListWithWeightOnADayUsingGET**
> kotlin.Array&lt;CarrierModel&gt; getDriverListWithWeightOnADayUsingGET(deliveryMode, timezoneOffset, pickUpDate, slot)

getDriverListWithWeightOnADay

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val deliveryMode : kotlin.String = deliveryMode_example // kotlin.String | deliveryMode
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val pickUpDate : kotlin.String = pickUpDate_example // kotlin.String | pickUpDate
val slot : kotlin.String = slot_example // kotlin.String | slot
try {
    val result : kotlin.Array<CarrierModel> = apiInstance.getDriverListWithWeightOnADayUsingGET(deliveryMode, timezoneOffset, pickUpDate, slot)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#getDriverListWithWeightOnADayUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#getDriverListWithWeightOnADayUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deliveryMode** | **kotlin.String**| deliveryMode |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **pickUpDate** | **kotlin.String**| pickUpDate | [optional]
 **slot** | **kotlin.String**| slot | [optional]

### Return type

[**kotlin.Array&lt;CarrierModel&gt;**](CarrierModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="retrieveUsingGET1"></a>
# **retrieveUsingGET1**
> kotlin.Array&lt;DriverMasterModel&gt; retrieveUsingGET1(category, regionId)

retrieve

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val category : kotlin.String = category_example // kotlin.String | category
val regionId : kotlin.Int = 56 // kotlin.Int | regionId
try {
    val result : kotlin.Array<DriverMasterModel> = apiInstance.retrieveUsingGET1(category, regionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#retrieveUsingGET1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#retrieveUsingGET1")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **category** | **kotlin.String**| category | [optional]
 **regionId** | **kotlin.Int**| regionId | [optional]

### Return type

[**kotlin.Array&lt;DriverMasterModel&gt;**](DriverMasterModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST3"></a>
# **updateUsingPOST3**
> kotlin.Array&lt;DriverMasterModel&gt; updateUsingPOST3(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = DriverManagementControllerApi()
val model : DriverManagementModelWrapper =  // DriverManagementModelWrapper | model
try {
    val result : kotlin.Array<DriverMasterModel> = apiInstance.updateUsingPOST3(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DriverManagementControllerApi#updateUsingPOST3")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DriverManagementControllerApi#updateUsingPOST3")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**DriverManagementModelWrapper**](DriverManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;DriverMasterModel&gt;**](DriverMasterModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

