# LogbackControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**disableDbLoggingUsingGET**](LogbackControllerApi.md#disableDbLoggingUsingGET) | **GET** /logback/disableDbLogging | disableDbLogging
[**enableDbLoggingUsingGET**](LogbackControllerApi.md#enableDbLoggingUsingGET) | **GET** /logback/enableDbLogging | enableDbLogging


<a name="disableDbLoggingUsingGET"></a>
# **disableDbLoggingUsingGET**
> kotlin.String disableDbLoggingUsingGET()

disableDbLogging

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = LogbackControllerApi()
try {
    val result : kotlin.String = apiInstance.disableDbLoggingUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling LogbackControllerApi#disableDbLoggingUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling LogbackControllerApi#disableDbLoggingUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="enableDbLoggingUsingGET"></a>
# **enableDbLoggingUsingGET**
> kotlin.String enableDbLoggingUsingGET()

enableDbLogging

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = LogbackControllerApi()
try {
    val result : kotlin.String = apiInstance.enableDbLoggingUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling LogbackControllerApi#enableDbLoggingUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling LogbackControllerApi#enableDbLoggingUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

