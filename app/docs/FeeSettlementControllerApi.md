# FeeSettlementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**retrieveUsingGET2**](FeeSettlementControllerApi.md#retrieveUsingGET2) | **GET** /feesettlement/feesettlement/retrieve | retrieve
[**updateUsingPOST4**](FeeSettlementControllerApi.md#updateUsingPOST4) | **POST** /feesettlement/feesettlement/update | update


<a name="retrieveUsingGET2"></a>
# **retrieveUsingGET2**
> kotlin.Array&lt;FeeSettlementModel&gt; retrieveUsingGET2(timezoneOffset, fromDate, senderId, toDate)

retrieve

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = FeeSettlementControllerApi()
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val fromDate : kotlin.String = fromDate_example // kotlin.String | fromDate
val senderId : kotlin.Int = 56 // kotlin.Int | senderId
val toDate : kotlin.String = toDate_example // kotlin.String | toDate
try {
    val result : kotlin.Array<FeeSettlementModel> = apiInstance.retrieveUsingGET2(timezoneOffset, fromDate, senderId, toDate)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FeeSettlementControllerApi#retrieveUsingGET2")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FeeSettlementControllerApi#retrieveUsingGET2")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **fromDate** | **kotlin.String**| fromDate | [optional]
 **senderId** | **kotlin.Int**| senderId | [optional]
 **toDate** | **kotlin.String**| toDate | [optional]

### Return type

[**kotlin.Array&lt;FeeSettlementModel&gt;**](FeeSettlementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST4"></a>
# **updateUsingPOST4**
> kotlin.Array&lt;FeeSettlementModel&gt; updateUsingPOST4(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = FeeSettlementControllerApi()
val model : FeeSettlementModelWrapper =  // FeeSettlementModelWrapper | model
try {
    val result : kotlin.Array<FeeSettlementModel> = apiInstance.updateUsingPOST4(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FeeSettlementControllerApi#updateUsingPOST4")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FeeSettlementControllerApi#updateUsingPOST4")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**FeeSettlementModelWrapper**](FeeSettlementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;FeeSettlementModel&gt;**](FeeSettlementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

