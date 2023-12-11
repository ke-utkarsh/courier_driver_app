# AppVersionControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getStatusUsingGET**](AppVersionControllerApi.md#getStatusUsingGET) | **GET** /appversion/getstatus | getStatus


<a name="getStatusUsingGET"></a>
# **getStatusUsingGET**
> AppVersionModel getStatusUsingGET(appVersion)

getStatus

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = AppVersionControllerApi()
val appVersion : kotlin.String = appVersion_example // kotlin.String | appVersion
try {
    val result : AppVersionModel = apiInstance.getStatusUsingGET(appVersion)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AppVersionControllerApi#getStatusUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AppVersionControllerApi#getStatusUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **appVersion** | **kotlin.String**| appVersion |

### Return type

[**AppVersionModel**](AppVersionModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

