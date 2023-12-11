# AwsMqttControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**pushMessageToAWSMQTTUsingPOST**](AwsMqttControllerApi.md#pushMessageToAWSMQTTUsingPOST) | **POST** /aws/pushMessageToAWSMQTT | pushMessageToAWSMQTT


<a name="pushMessageToAWSMQTTUsingPOST"></a>
# **pushMessageToAWSMQTTUsingPOST**
> kotlin.String pushMessageToAWSMQTTUsingPOST(payload)

pushMessageToAWSMQTT

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = AwsMqttControllerApi()
val payload : kotlin.String = payload_example // kotlin.String | payload
try {
    val result : kotlin.String = apiInstance.pushMessageToAWSMQTTUsingPOST(payload)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AwsMqttControllerApi#pushMessageToAWSMQTTUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AwsMqttControllerApi#pushMessageToAWSMQTTUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payload** | **kotlin.String**| payload |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

