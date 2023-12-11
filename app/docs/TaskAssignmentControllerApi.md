# TaskAssignmentControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getTaskAssignmentListUsingGET**](TaskAssignmentControllerApi.md#getTaskAssignmentListUsingGET) | **GET** /task/taskassignment/retrieve | getTaskAssignmentList


<a name="getTaskAssignmentListUsingGET"></a>
# **getTaskAssignmentListUsingGET**
> kotlin.Any getTaskAssignmentListUsingGET(deliveryMode, district, orderNo, region, taskId, taskSequence)

getTaskAssignmentList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = TaskAssignmentControllerApi()
val deliveryMode : kotlin.String = deliveryMode_example // kotlin.String | deliveryMode
val district : kotlin.String = district_example // kotlin.String | district
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val region : kotlin.String = region_example // kotlin.String | region
val taskId : kotlin.String = taskId_example // kotlin.String | taskId
val taskSequence : kotlin.Int = 56 // kotlin.Int | taskSequence
try {
    val result : kotlin.Any = apiInstance.getTaskAssignmentListUsingGET(deliveryMode, district, orderNo, region, taskId, taskSequence)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TaskAssignmentControllerApi#getTaskAssignmentListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TaskAssignmentControllerApi#getTaskAssignmentListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deliveryMode** | **kotlin.String**| deliveryMode |
 **district** | **kotlin.String**| district |
 **orderNo** | **kotlin.String**| orderNo |
 **region** | **kotlin.String**| region |
 **taskId** | **kotlin.String**| taskId |
 **taskSequence** | **kotlin.Int**| taskSequence |

### Return type

[**kotlin.Any**](kotlin.Any.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

