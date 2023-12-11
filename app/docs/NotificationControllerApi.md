# NotificationControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**sendFcmNotificationToSingleDeviceUsingPOST**](NotificationControllerApi.md#sendFcmNotificationToSingleDeviceUsingPOST) | **POST** /notification/sendFcmNotificationToSingleDevice | sendFcmNotificationToSingleDevice
[**sendFcmNotificationToTopicUsingPOST**](NotificationControllerApi.md#sendFcmNotificationToTopicUsingPOST) | **POST** /notification/sendFcmNotificationToTopic | sendFcmNotificationToTopic


<a name="sendFcmNotificationToSingleDeviceUsingPOST"></a>
# **sendFcmNotificationToSingleDeviceUsingPOST**
> kotlin.String sendFcmNotificationToSingleDeviceUsingPOST(oNotificationRequest)

sendFcmNotificationToSingleDevice

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = NotificationControllerApi()
val oNotificationRequest : FCMNotificationRequestForSingleDevice =  // FCMNotificationRequestForSingleDevice | oNotificationRequest
try {
    val result : kotlin.String = apiInstance.sendFcmNotificationToSingleDeviceUsingPOST(oNotificationRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling NotificationControllerApi#sendFcmNotificationToSingleDeviceUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling NotificationControllerApi#sendFcmNotificationToSingleDeviceUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oNotificationRequest** | [**FCMNotificationRequestForSingleDevice**](FCMNotificationRequestForSingleDevice.md)| oNotificationRequest |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="sendFcmNotificationToTopicUsingPOST"></a>
# **sendFcmNotificationToTopicUsingPOST**
> kotlin.String sendFcmNotificationToTopicUsingPOST(oNotificationRequest)

sendFcmNotificationToTopic

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = NotificationControllerApi()
val oNotificationRequest : FCMNotificationRequestForTopic =  // FCMNotificationRequestForTopic | oNotificationRequest
try {
    val result : kotlin.String = apiInstance.sendFcmNotificationToTopicUsingPOST(oNotificationRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling NotificationControllerApi#sendFcmNotificationToTopicUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling NotificationControllerApi#sendFcmNotificationToTopicUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oNotificationRequest** | [**FCMNotificationRequestForTopic**](FCMNotificationRequestForTopic.md)| oNotificationRequest |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

