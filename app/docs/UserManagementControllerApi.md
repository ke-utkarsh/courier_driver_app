# UserManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changePasswordUsingPUT**](UserManagementControllerApi.md#changePasswordUsingPUT) | **PUT** /user/usermanagement/changePassword | changePassword
[**retrieveUsingGET3**](UserManagementControllerApi.md#retrieveUsingGET3) | **GET** /user/usermanagement/retrieve | retrieve
[**updateUsingPOST6**](UserManagementControllerApi.md#updateUsingPOST6) | **POST** /user/usermanagement/update | update


<a name="changePasswordUsingPUT"></a>
# **changePasswordUsingPUT**
> ChangePasswordResultDTO changePasswordUsingPUT(oChangePassword)

changePassword

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserManagementControllerApi()
val oChangePassword : ChangePasswordDTO =  // ChangePasswordDTO | oChangePassword
try {
    val result : ChangePasswordResultDTO = apiInstance.changePasswordUsingPUT(oChangePassword)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserManagementControllerApi#changePasswordUsingPUT")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserManagementControllerApi#changePasswordUsingPUT")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oChangePassword** | [**ChangePasswordDTO**](ChangePasswordDTO.md)| oChangePassword |

### Return type

[**ChangePasswordResultDTO**](ChangePasswordResultDTO.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="retrieveUsingGET3"></a>
# **retrieveUsingGET3**
> kotlin.Array&lt;UserMaster&gt; retrieveUsingGET3(mobileNo, username)

retrieve

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserManagementControllerApi()
val mobileNo : kotlin.String = mobileNo_example // kotlin.String | mobileNo
val username : kotlin.String = username_example // kotlin.String | username
try {
    val result : kotlin.Array<UserMaster> = apiInstance.retrieveUsingGET3(mobileNo, username)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserManagementControllerApi#retrieveUsingGET3")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserManagementControllerApi#retrieveUsingGET3")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mobileNo** | **kotlin.String**| mobileNo | [optional]
 **username** | **kotlin.String**| username | [optional]

### Return type

[**kotlin.Array&lt;UserMaster&gt;**](UserMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST6"></a>
# **updateUsingPOST6**
> kotlin.Array&lt;UserMaster&gt; updateUsingPOST6(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserManagementControllerApi()
val model : UserManagementModelWrapper =  // UserManagementModelWrapper | model
try {
    val result : kotlin.Array<UserMaster> = apiInstance.updateUsingPOST6(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserManagementControllerApi#updateUsingPOST6")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserManagementControllerApi#updateUsingPOST6")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**UserManagementModelWrapper**](UserManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;UserMaster&gt;**](UserMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

