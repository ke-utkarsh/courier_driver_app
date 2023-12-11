# LoginControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**authenticateUserUsingPOST**](LoginControllerApi.md#authenticateUserUsingPOST) | **POST** /login | authenticateUser


<a name="authenticateUserUsingPOST"></a>
# **authenticateUserUsingPOST**
> APIResponse authenticateUserUsingPOST(user)

authenticateUser

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = LoginControllerApi()
val user : UserMaster =  // UserMaster | user
try {
    val result : APIResponse = apiInstance.authenticateUserUsingPOST(user)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling LoginControllerApi#authenticateUserUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling LoginControllerApi#authenticateUserUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **user** | [**UserMaster**](UserMaster.md)| user |

### Return type

[**APIResponse**](APIResponse.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

