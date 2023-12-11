# PrintOrderSlipControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**generatePdfFromTemplateUsingGET**](PrintOrderSlipControllerApi.md#generatePdfFromTemplateUsingGET) | **GET** /order/printorderslip/generatePdfFromTemplate | generatePdfFromTemplate
[**getAllOrderListUsingGET1**](PrintOrderSlipControllerApi.md#getAllOrderListUsingGET1) | **GET** /order/printorderslip/retrieve | getAllOrderList


<a name="generatePdfFromTemplateUsingGET"></a>
# **generatePdfFromTemplateUsingGET**
> ByteArray generatePdfFromTemplateUsingGET(requiredAll, timezoneOffset, fromDate, orderIds, orderNo, toDate)

generatePdfFromTemplate

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = PrintOrderSlipControllerApi()
val requiredAll : kotlin.Int = 56 // kotlin.Int | requiredAll
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val fromDate : kotlin.String = fromDate_example // kotlin.String | fromDate
val orderIds : kotlin.Array<kotlin.Long> =  // kotlin.Array<kotlin.Long> | orderIds
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val toDate : kotlin.String = toDate_example // kotlin.String | toDate
try {
    val result : ByteArray = apiInstance.generatePdfFromTemplateUsingGET(requiredAll, timezoneOffset, fromDate, orderIds, orderNo, toDate)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PrintOrderSlipControllerApi#generatePdfFromTemplateUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PrintOrderSlipControllerApi#generatePdfFromTemplateUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requiredAll** | **kotlin.Int**| requiredAll |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **fromDate** | **kotlin.String**| fromDate | [optional]
 **orderIds** | [**kotlin.Array&lt;kotlin.Long&gt;**](kotlin.Long.md)| orderIds | [optional]
 **orderNo** | **kotlin.String**| orderNo | [optional]
 **toDate** | **kotlin.String**| toDate | [optional]

### Return type

[**ByteArray**](ByteArray.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getAllOrderListUsingGET1"></a>
# **getAllOrderListUsingGET1**
> kotlin.Array&lt;OrderManagementModel&gt; getAllOrderListUsingGET1(fromDate, timezoneOffset, toDate, orderNo)

getAllOrderList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = PrintOrderSlipControllerApi()
val fromDate : kotlin.String = fromDate_example // kotlin.String | fromDate
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val toDate : kotlin.String = toDate_example // kotlin.String | toDate
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
try {
    val result : kotlin.Array<OrderManagementModel> = apiInstance.getAllOrderListUsingGET1(fromDate, timezoneOffset, toDate, orderNo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PrintOrderSlipControllerApi#getAllOrderListUsingGET1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PrintOrderSlipControllerApi#getAllOrderListUsingGET1")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fromDate** | **kotlin.String**| fromDate |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **toDate** | **kotlin.String**| toDate |
 **orderNo** | **kotlin.String**| orderNo | [optional]

### Return type

[**kotlin.Array&lt;OrderManagementModel&gt;**](OrderManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

