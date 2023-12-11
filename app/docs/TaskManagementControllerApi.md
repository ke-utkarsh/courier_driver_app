# TaskManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllAvailableDriverUsingGET**](TaskManagementControllerApi.md#getAllAvailableDriverUsingGET) | **GET** /task/taskmanagement/getAllAvailableDriver | getAllAvailableDriver
[**getAppDataUsingPOST**](TaskManagementControllerApi.md#getAppDataUsingPOST) | **POST** /task/taskmanagement/retrieve | getAppData
[**getDriverTaskListUsingGET**](TaskManagementControllerApi.md#getDriverTaskListUsingGET) | **GET** /task/taskmanagement/retrieveDriverTasks | getDriverTaskList
[**updateUsingPOST5**](TaskManagementControllerApi.md#updateUsingPOST5) | **POST** /task/taskmanagement/update | update


<a name="getAllAvailableDriverUsingGET"></a>
# **getAllAvailableDriverUsingGET**
> kotlin.Array&lt;CarrierModel&gt; getAllAvailableDriverUsingGET(category, pickUpDate, slot, timezoneOffset)

getAllAvailableDriver

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = TaskManagementControllerApi()
val category : kotlin.String = category_example // kotlin.String | category
val pickUpDate : kotlin.String = pickUpDate_example // kotlin.String | pickUpDate
val slot : kotlin.String = slot_example // kotlin.String | slot
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : kotlin.Array<CarrierModel> = apiInstance.getAllAvailableDriverUsingGET(category, pickUpDate, slot, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TaskManagementControllerApi#getAllAvailableDriverUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TaskManagementControllerApi#getAllAvailableDriverUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **category** | **kotlin.String**| category |
 **pickUpDate** | **kotlin.String**| pickUpDate |
 **slot** | **kotlin.String**| slot |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |

### Return type

[**kotlin.Array&lt;CarrierModel&gt;**](CarrierModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAppDataUsingPOST"></a>
# **getAppDataUsingPOST**
> kotlin.Array&lt;AppDataModel&gt; getAppDataUsingPOST(taskListParameterModel)

getAppData

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = TaskManagementControllerApi()
val taskListParameterModel : AppQueryModel =  // AppQueryModel | taskListParameterModel
try {
    val result : kotlin.Array<AppDataModel> = apiInstance.getAppDataUsingPOST(taskListParameterModel)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TaskManagementControllerApi#getAppDataUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TaskManagementControllerApi#getAppDataUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **taskListParameterModel** | [**AppQueryModel**](AppQueryModel.md)| taskListParameterModel |

### Return type

[**kotlin.Array&lt;AppDataModel&gt;**](AppDataModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDriverTaskListUsingGET"></a>
# **getDriverTaskListUsingGET**
> kotlin.Array&lt;TaskManagementModel&gt; getDriverTaskListUsingGET(timezoneOffset, orderNo, taskStatus)

getDriverTaskList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = TaskManagementControllerApi()
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val taskStatus : kotlin.Int = 56 // kotlin.Int | taskStatus
try {
    val result : kotlin.Array<TaskManagementModel> = apiInstance.getDriverTaskListUsingGET(timezoneOffset, orderNo, taskStatus)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TaskManagementControllerApi#getDriverTaskListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TaskManagementControllerApi#getDriverTaskListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **orderNo** | **kotlin.String**| orderNo | [optional]
 **taskStatus** | **kotlin.Int**| taskStatus | [optional]

### Return type

[**kotlin.Array&lt;TaskManagementModel&gt;**](TaskManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateUsingPOST5"></a>
# **updateUsingPOST5**
> kotlin.Array&lt;TaskManagementModel&gt; updateUsingPOST5(model)

update

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = TaskManagementControllerApi()
val model : TaskManagementModelWrapper =  // TaskManagementModelWrapper | model
try {
    val result : kotlin.Array<TaskManagementModel> = apiInstance.updateUsingPOST5(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TaskManagementControllerApi#updateUsingPOST5")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TaskManagementControllerApi#updateUsingPOST5")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**TaskManagementModelWrapper**](TaskManagementModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;TaskManagementModel&gt;**](TaskManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

