# ApiDetailControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getApiConfigurationUsingGET**](ApiDetailControllerApi.md#getApiConfigurationUsingGET) | **GET** /apiConfig/getApiConfiguration | getApiConfiguration


<a name="getApiConfigurationUsingGET"></a>
# **getApiConfigurationUsingGET**
> APIConfigurationDTO getApiConfigurationUsingGET()

getApiConfiguration

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = ApiDetailControllerApi()
try {
    val result : APIConfigurationDTO = apiInstance.getApiConfigurationUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ApiDetailControllerApi#getApiConfigurationUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ApiDetailControllerApi#getApiConfigurationUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**APIConfigurationDTO**](APIConfigurationDTO.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

