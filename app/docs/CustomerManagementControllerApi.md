# CustomerManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteCheckForCustomerAddressUsingGET**](CustomerManagementControllerApi.md#deleteCheckForCustomerAddressUsingGET) | **GET** /customer/customermanagement/deleteCheckForCustomerAddress | deleteCheckForCustomerAddress
[**deleteCheckForCustomerUsingGET**](CustomerManagementControllerApi.md#deleteCheckForCustomerUsingGET) | **GET** /customer/customermanagement/deleteCheckForCustomer | deleteCheckForCustomer
[**deleteCustomerUsingPOST**](CustomerManagementControllerApi.md#deleteCustomerUsingPOST) | **POST** /customer/customermanagement/deleteCustomer | deleteCustomer
[**getCustomerDetailsOnNameUsingGET**](CustomerManagementControllerApi.md#getCustomerDetailsOnNameUsingGET) | **GET** /customer/customermanagement/getCustomerDetailOnName | getCustomerDetailsOnName
[**getCustomerInfoByNameAndPhoneNoUsingGET**](CustomerManagementControllerApi.md#getCustomerInfoByNameAndPhoneNoUsingGET) | **GET** /customer/customermanagement/getcustomerInfoByNameAndPhoneNo | getCustomerInfoByNameAndPhoneNo
[**getCustomerInfoOnIdUsingGET**](CustomerManagementControllerApi.md#getCustomerInfoOnIdUsingGET) | **GET** /customer/customermanagement/getCustomerInfOonId | getCustomerInfoOnId
[**getCustomerInfoOnPhoneNoUsingGET**](CustomerManagementControllerApi.md#getCustomerInfoOnPhoneNoUsingGET) | **GET** /customer/customermanagement/getcustomerInfoOnPhoneNo | getCustomerInfoOnPhoneNo
[**updateCustomerAddressUsingPOST**](CustomerManagementControllerApi.md#updateCustomerAddressUsingPOST) | **POST** /customer/customermanagement/updateCustomerAddress | updateCustomerAddress
[**updateCustomerInfoUsingPOST**](CustomerManagementControllerApi.md#updateCustomerInfoUsingPOST) | **POST** /customer/customermanagement/updatecustomerinfo | updateCustomerInfo


<a name="deleteCheckForCustomerAddressUsingGET"></a>
# **deleteCheckForCustomerAddressUsingGET**
> kotlin.Boolean deleteCheckForCustomerAddressUsingGET(addressId)

deleteCheckForCustomerAddress

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val addressId : kotlin.Long = 789 // kotlin.Long | addressId
try {
    val result : kotlin.Boolean = apiInstance.deleteCheckForCustomerAddressUsingGET(addressId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#deleteCheckForCustomerAddressUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#deleteCheckForCustomerAddressUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **addressId** | **kotlin.Long**| addressId |

### Return type

**kotlin.Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteCheckForCustomerUsingGET"></a>
# **deleteCheckForCustomerUsingGET**
> kotlin.Boolean deleteCheckForCustomerUsingGET(customerId)

deleteCheckForCustomer

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val customerId : kotlin.Int = 56 // kotlin.Int | customerId
try {
    val result : kotlin.Boolean = apiInstance.deleteCheckForCustomerUsingGET(customerId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#deleteCheckForCustomerUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#deleteCheckForCustomerUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **customerId** | **kotlin.Int**| customerId |

### Return type

**kotlin.Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteCustomerUsingPOST"></a>
# **deleteCustomerUsingPOST**
> kotlin.Array&lt;CustomerManagementModel&gt; deleteCustomerUsingPOST(model)

deleteCustomer

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val model : CustomerManagementModelWrapper =  // CustomerManagementModelWrapper | model
try {
    val result : kotlin.Array<CustomerManagementModel> = apiInstance.deleteCustomerUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#deleteCustomerUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#deleteCustomerUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**CustomerManagementModelWrapper**](CustomerManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;CustomerManagementModel&gt;**](CustomerManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCustomerDetailsOnNameUsingGET"></a>
# **getCustomerDetailsOnNameUsingGET**
> kotlin.Array&lt;CustomerManagementModel&gt; getCustomerDetailsOnNameUsingGET(name)

getCustomerDetailsOnName

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val name : kotlin.String = name_example // kotlin.String | name
try {
    val result : kotlin.Array<CustomerManagementModel> = apiInstance.getCustomerDetailsOnNameUsingGET(name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#getCustomerDetailsOnNameUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#getCustomerDetailsOnNameUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **kotlin.String**| name | [optional]

### Return type

[**kotlin.Array&lt;CustomerManagementModel&gt;**](CustomerManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCustomerInfoByNameAndPhoneNoUsingGET"></a>
# **getCustomerInfoByNameAndPhoneNoUsingGET**
> kotlin.Array&lt;CustomerInfoModel&gt; getCustomerInfoByNameAndPhoneNoUsingGET(name, phoneNo)

getCustomerInfoByNameAndPhoneNo

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val name : kotlin.String = name_example // kotlin.String | name
val phoneNo : kotlin.String = phoneNo_example // kotlin.String | phoneNo
try {
    val result : kotlin.Array<CustomerInfoModel> = apiInstance.getCustomerInfoByNameAndPhoneNoUsingGET(name, phoneNo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#getCustomerInfoByNameAndPhoneNoUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#getCustomerInfoByNameAndPhoneNoUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **kotlin.String**| name | [optional]
 **phoneNo** | **kotlin.String**| phoneNo | [optional]

### Return type

[**kotlin.Array&lt;CustomerInfoModel&gt;**](CustomerInfoModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCustomerInfoOnIdUsingGET"></a>
# **getCustomerInfoOnIdUsingGET**
> CustomerInfoModelWrapper getCustomerInfoOnIdUsingGET(customerId)

getCustomerInfoOnId

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val customerId : kotlin.Int = 56 // kotlin.Int | customerId
try {
    val result : CustomerInfoModelWrapper = apiInstance.getCustomerInfoOnIdUsingGET(customerId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#getCustomerInfoOnIdUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#getCustomerInfoOnIdUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **customerId** | **kotlin.Int**| customerId |

### Return type

[**CustomerInfoModelWrapper**](CustomerInfoModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCustomerInfoOnPhoneNoUsingGET"></a>
# **getCustomerInfoOnPhoneNoUsingGET**
> kotlin.Array&lt;CustomerInfoModel&gt; getCustomerInfoOnPhoneNoUsingGET(phoneNo, flag)

getCustomerInfoOnPhoneNo

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val phoneNo : kotlin.String = phoneNo_example // kotlin.String | phoneNo
val flag : kotlin.String = flag_example // kotlin.String | flag
try {
    val result : kotlin.Array<CustomerInfoModel> = apiInstance.getCustomerInfoOnPhoneNoUsingGET(phoneNo, flag)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#getCustomerInfoOnPhoneNoUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#getCustomerInfoOnPhoneNoUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **phoneNo** | **kotlin.String**| phoneNo |
 **flag** | **kotlin.String**| flag | [optional]

### Return type

[**kotlin.Array&lt;CustomerInfoModel&gt;**](CustomerInfoModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateCustomerAddressUsingPOST"></a>
# **updateCustomerAddressUsingPOST**
> kotlin.Array&lt;CustomerManagementModel&gt; updateCustomerAddressUsingPOST(model)

updateCustomerAddress

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val model : CustomerManagementModelWrapper =  // CustomerManagementModelWrapper | model
try {
    val result : kotlin.Array<CustomerManagementModel> = apiInstance.updateCustomerAddressUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#updateCustomerAddressUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#updateCustomerAddressUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**CustomerManagementModelWrapper**](CustomerManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;CustomerManagementModel&gt;**](CustomerManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateCustomerInfoUsingPOST"></a>
# **updateCustomerInfoUsingPOST**
> CustomerInfoModelWrapper updateCustomerInfoUsingPOST(model)

updateCustomerInfo

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CustomerManagementControllerApi()
val model : AddCustomerModelWrapper =  // AddCustomerModelWrapper | model
try {
    val result : CustomerInfoModelWrapper = apiInstance.updateCustomerInfoUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CustomerManagementControllerApi#updateCustomerInfoUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CustomerManagementControllerApi#updateCustomerInfoUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**AddCustomerModelWrapper**](AddCustomerModelWrapper.md)| model |

### Return type

[**CustomerInfoModelWrapper**](CustomerInfoModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

