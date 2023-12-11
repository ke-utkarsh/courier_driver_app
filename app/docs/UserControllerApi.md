# UserControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changePasswordUsingPUT1**](UserControllerApi.md#changePasswordUsingPUT1) | **PUT** /user/changePassword | changePassword
[**createUserUsingPOST**](UserControllerApi.md#createUserUsingPOST) | **POST** /user/create | createUser
[**deleteUserUsingDELETE**](UserControllerApi.md#deleteUserUsingDELETE) | **DELETE** /user/delete/{id} | deleteUser
[**getPortalUsersUsingGET**](UserControllerApi.md#getPortalUsersUsingGET) | **GET** /user/getUsers | getPortalUsers
[**updateUserUsingPUT**](UserControllerApi.md#updateUserUsingPUT) | **PUT** /user/update | updateUser


<a name="changePasswordUsingPUT1"></a>
# **changePasswordUsingPUT1**
> kotlin.String changePasswordUsingPUT1(oChangePassword)

changePassword

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserControllerApi()
val oChangePassword : ChangePasswordDTO =  // ChangePasswordDTO | oChangePassword
try {
    val result : kotlin.String = apiInstance.changePasswordUsingPUT1(oChangePassword)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserControllerApi#changePasswordUsingPUT1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserControllerApi#changePasswordUsingPUT1")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oChangePassword** | [**ChangePasswordDTO**](ChangePasswordDTO.md)| oChangePassword |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="createUserUsingPOST"></a>
# **createUserUsingPOST**
> kotlin.String createUserUsingPOST(user)

createUser

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserControllerApi()
val user : UserMaster =  // UserMaster | user
try {
    val result : kotlin.String = apiInstance.createUserUsingPOST(user)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserControllerApi#createUserUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserControllerApi#createUserUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **user** | [**UserMaster**](UserMaster.md)| user |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="deleteUserUsingDELETE"></a>
# **deleteUserUsingDELETE**
> kotlin.String deleteUserUsingDELETE(id)

deleteUser

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserControllerApi()
val id : kotlin.String = id_example // kotlin.String | id
try {
    val result : kotlin.String = apiInstance.deleteUserUsingDELETE(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserControllerApi#deleteUserUsingDELETE")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserControllerApi#deleteUserUsingDELETE")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| id |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getPortalUsersUsingGET"></a>
# **getPortalUsersUsingGET**
> kotlin.Array&lt;UserMaster&gt; getPortalUsersUsingGET()

getPortalUsers

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserControllerApi()
try {
    val result : kotlin.Array<UserMaster> = apiInstance.getPortalUsersUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserControllerApi#getPortalUsersUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserControllerApi#getPortalUsersUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;UserMaster&gt;**](UserMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUserUsingPUT"></a>
# **updateUserUsingPUT**
> kotlin.String updateUserUsingPUT(user)

updateUser

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = UserControllerApi()
val user : UserMaster =  // UserMaster | user
try {
    val result : kotlin.String = apiInstance.updateUserUsingPUT(user)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UserControllerApi#updateUserUsingPUT")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UserControllerApi#updateUserUsingPUT")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **user** | [**UserMaster**](UserMaster.md)| user |

### Return type

**kotlin.String**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

