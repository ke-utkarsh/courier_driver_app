# AppModuleControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllUserModuleListUsingGET**](AppModuleControllerApi.md#getAllUserModuleListUsingGET) | **GET** /appmodule/getModuleList | getAllUserModuleList


<a name="getAllUserModuleListUsingGET"></a>
# **getAllUserModuleListUsingGET**
> kotlin.Array&lt;AppModuleMaster&gt; getAllUserModuleListUsingGET(roleId)

getAllUserModuleList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = AppModuleControllerApi()
val roleId : kotlin.Int = 56 // kotlin.Int | roleId
try {
    val result : kotlin.Array<AppModuleMaster> = apiInstance.getAllUserModuleListUsingGET(roleId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AppModuleControllerApi#getAllUserModuleListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AppModuleControllerApi#getAllUserModuleListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **roleId** | **kotlin.Int**| roleId |

### Return type

[**kotlin.Array&lt;AppModuleMaster&gt;**](AppModuleMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

