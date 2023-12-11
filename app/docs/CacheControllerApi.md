# CacheControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**evictCacheUsingPOST**](CacheControllerApi.md#evictCacheUsingPOST) | **POST** /cache/evict | evictCache


<a name="evictCacheUsingPOST"></a>
# **evictCacheUsingPOST**
> kotlin.String evictCacheUsingPOST(cacheList)

evictCache

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CacheControllerApi()
val cacheList : kotlin.Array<kotlin.String> =  // kotlin.Array<kotlin.String> | cacheList
try {
    val result : kotlin.String = apiInstance.evictCacheUsingPOST(cacheList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CacheControllerApi#evictCacheUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CacheControllerApi#evictCacheUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **cacheList** | **kotlin.Array&lt;kotlin.String&gt;**| cacheList |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

