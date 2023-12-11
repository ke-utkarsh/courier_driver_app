# NotificationManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**generatePacketsToSendNotificationUsingPOST**](NotificationManagementControllerApi.md#generatePacketsToSendNotificationUsingPOST) | **POST** /notification/notificationmanagement/generatePacketsToSendNotification | generatePacketsToSendNotification


<a name="generatePacketsToSendNotificationUsingPOST"></a>
# **generatePacketsToSendNotificationUsingPOST**
> kotlin.String generatePacketsToSendNotificationUsingPOST(modifiedOrderData)

generatePacketsToSendNotification

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = NotificationManagementControllerApi()
val modifiedOrderData : NotificationManagementModelWrapper =  // NotificationManagementModelWrapper | modifiedOrderData
try {
    val result : kotlin.String = apiInstance.generatePacketsToSendNotificationUsingPOST(modifiedOrderData)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling NotificationManagementControllerApi#generatePacketsToSendNotificationUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling NotificationManagementControllerApi#generatePacketsToSendNotificationUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **modifiedOrderData** | [**NotificationManagementModelWrapper**](NotificationManagementModelWrapper.md)| modifiedOrderData |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

