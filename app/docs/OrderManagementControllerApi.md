# OrderManagementControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**copyOrderByOrderManagementUsingGET**](OrderManagementControllerApi.md#copyOrderByOrderManagementUsingGET) | **GET** /order/ordermanagement/copyOrderByOrderManagement | copyOrderByOrderManagement
[**deleteOrderUsingGET**](OrderManagementControllerApi.md#deleteOrderUsingGET) | **GET** /order/ordermanagement/deleteOrder | deleteOrder
[**doImportUsingPOST**](OrderManagementControllerApi.md#doImportUsingPOST) | **POST** /order/ordermanagement/importfile | doImport
[**getAllOrderListUsingGET**](OrderManagementControllerApi.md#getAllOrderListUsingGET) | **GET** /order/ordermanagement/retrieve | getAllOrderList
[**getCopyOrderByOrderDetailUsingGET**](OrderManagementControllerApi.md#getCopyOrderByOrderDetailUsingGET) | **GET** /order/ordermanagement/getCopyOrderByOrderDetail | getCopyOrderByOrderDetail
[**getDefaultOrderTaskListUsingGET**](OrderManagementControllerApi.md#getDefaultOrderTaskListUsingGET) | **GET** /order/ordermanagement/getDefaultOrderTaskList | getDefaultOrderTaskList
[**getOrderDetailList1UsingGET**](OrderManagementControllerApi.md#getOrderDetailList1UsingGET) | **GET** /order/ordermanagement/getOrderDetail | getOrderDetailList1
[**getOrderMemoUsingGET**](OrderManagementControllerApi.md#getOrderMemoUsingGET) | **GET** /order/ordermanagement/getOrderMemo | getOrderMemo
[**getPaymentDetailsUsingGET**](OrderManagementControllerApi.md#getPaymentDetailsUsingGET) | **GET** /order/ordermanagement/getpaymentDetails | getPaymentDetails
[**getSenderListUsingGET**](OrderManagementControllerApi.md#getSenderListUsingGET) | **GET** /order/ordermanagement/getSenderList | getSenderList
[**getUpdateUsingPOST**](OrderManagementControllerApi.md#getUpdateUsingPOST) | **POST** /order/ordermanagement/updateOrder | getUpdate
[**updateOrderAndTaskStatusUsingPOST**](OrderManagementControllerApi.md#updateOrderAndTaskStatusUsingPOST) | **POST** /order/ordermanagement/updateorderandtask | updateOrderAndTaskStatus
[**updateOrderMemoUsingPOST**](OrderManagementControllerApi.md#updateOrderMemoUsingPOST) | **POST** /order/ordermanagement/updateOrderMemo | updateOrderMemo
[**updateOrdersUsingPOST**](OrderManagementControllerApi.md#updateOrdersUsingPOST) | **POST** /order/ordermanagement/update | updateOrders
[**updatePaymentDetailsUsingPOST**](OrderManagementControllerApi.md#updatePaymentDetailsUsingPOST) | **POST** /order/ordermanagement/updatepaymentDetails | updatePaymentDetails


<a name="copyOrderByOrderManagementUsingGET"></a>
# **copyOrderByOrderManagementUsingGET**
> kotlin.Array&lt;OrderManagementModel&gt; copyOrderByOrderManagementUsingGET(orderIdList, timezoneOffset, district, fromDate, orderNo, orderStatusIds, orderType, senderName, toDate, userName, yearMonth)

copyOrderByOrderManagement

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderIdList : kotlin.Array<kotlin.Long> =  // kotlin.Array<kotlin.Long> | orderIdList
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val district : kotlin.Int = 56 // kotlin.Int | district
val fromDate : kotlin.String = fromDate_example // kotlin.String | fromDate
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val orderStatusIds : kotlin.Array<kotlin.Int> =  // kotlin.Array<kotlin.Int> | orderStatusIds
val orderType : kotlin.Int = 56 // kotlin.Int | orderType
val senderName : kotlin.String = senderName_example // kotlin.String | senderName
val toDate : kotlin.String = toDate_example // kotlin.String | toDate
val userName : kotlin.String = userName_example // kotlin.String | userName
val yearMonth : kotlin.String = yearMonth_example // kotlin.String | yearMonth
try {
    val result : kotlin.Array<OrderManagementModel> = apiInstance.copyOrderByOrderManagementUsingGET(orderIdList, timezoneOffset, district, fromDate, orderNo, orderStatusIds, orderType, senderName, toDate, userName, yearMonth)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#copyOrderByOrderManagementUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#copyOrderByOrderManagementUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderIdList** | [**kotlin.Array&lt;kotlin.Long&gt;**](kotlin.Long.md)| orderIdList |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **district** | **kotlin.Int**| district | [optional]
 **fromDate** | **kotlin.String**| fromDate | [optional]
 **orderNo** | **kotlin.String**| orderNo | [optional]
 **orderStatusIds** | [**kotlin.Array&lt;kotlin.Int&gt;**](kotlin.Int.md)| orderStatusIds | [optional]
 **orderType** | **kotlin.Int**| orderType | [optional]
 **senderName** | **kotlin.String**| senderName | [optional]
 **toDate** | **kotlin.String**| toDate | [optional]
 **userName** | **kotlin.String**| userName | [optional]
 **yearMonth** | **kotlin.String**| yearMonth | [optional]

### Return type

[**kotlin.Array&lt;OrderManagementModel&gt;**](OrderManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteOrderUsingGET"></a>
# **deleteOrderUsingGET**
> deleteOrderUsingGET(orderId)

deleteOrder

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderId : kotlin.Long = 789 // kotlin.Long | orderId
try {
    apiInstance.deleteOrderUsingGET(orderId)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#deleteOrderUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#deleteOrderUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **kotlin.Long**| orderId |

### Return type

null (empty response body)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="doImportUsingPOST"></a>
# **doImportUsingPOST**
> kotlin.Array&lt;OrderManagementModel&gt; doImportUsingPOST(model)

doImport

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val model : OrderManagementImportModelWrapper =  // OrderManagementImportModelWrapper | model
try {
    val result : kotlin.Array<OrderManagementModel> = apiInstance.doImportUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#doImportUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#doImportUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**OrderManagementImportModelWrapper**](OrderManagementImportModelWrapper.md)| model |

### Return type

[**kotlin.Array&lt;OrderManagementModel&gt;**](OrderManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getAllOrderListUsingGET"></a>
# **getAllOrderListUsingGET**
> kotlin.Array&lt;OrderManagementModel&gt; getAllOrderListUsingGET(timezoneOffset, district, fromDate, orderNo, orderStatusIds, orderType, senderName, toDate, yearMonth)

getAllOrderList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val district : kotlin.Int = 56 // kotlin.Int | district
val fromDate : kotlin.String = fromDate_example // kotlin.String | fromDate
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val orderStatusIds : kotlin.Array<kotlin.Int> =  // kotlin.Array<kotlin.Int> | orderStatusIds
val orderType : kotlin.Int = 56 // kotlin.Int | orderType
val senderName : kotlin.String = senderName_example // kotlin.String | senderName
val toDate : kotlin.String = toDate_example // kotlin.String | toDate
val yearMonth : kotlin.String = yearMonth_example // kotlin.String | yearMonth
try {
    val result : kotlin.Array<OrderManagementModel> = apiInstance.getAllOrderListUsingGET(timezoneOffset, district, fromDate, orderNo, orderStatusIds, orderType, senderName, toDate, yearMonth)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getAllOrderListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getAllOrderListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **district** | **kotlin.Int**| district | [optional]
 **fromDate** | **kotlin.String**| fromDate | [optional]
 **orderNo** | **kotlin.String**| orderNo | [optional]
 **orderStatusIds** | [**kotlin.Array&lt;kotlin.Int&gt;**](kotlin.Int.md)| orderStatusIds | [optional]
 **orderType** | **kotlin.Int**| orderType | [optional]
 **senderName** | **kotlin.String**| senderName | [optional]
 **toDate** | **kotlin.String**| toDate | [optional]
 **yearMonth** | **kotlin.String**| yearMonth | [optional]

### Return type

[**kotlin.Array&lt;OrderManagementModel&gt;**](OrderManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCopyOrderByOrderDetailUsingGET"></a>
# **getCopyOrderByOrderDetailUsingGET**
> OrderDetailIListInfoModelWrapper getCopyOrderByOrderDetailUsingGET(orderid, timezoneOffset, userName)

getCopyOrderByOrderDetail

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderid : kotlin.Long = 789 // kotlin.Long | orderid
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val userName : kotlin.String = userName_example // kotlin.String | userName
try {
    val result : OrderDetailIListInfoModelWrapper = apiInstance.getCopyOrderByOrderDetailUsingGET(orderid, timezoneOffset, userName)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getCopyOrderByOrderDetailUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getCopyOrderByOrderDetailUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderid** | **kotlin.Long**| orderid |
 **timezoneOffset** | **kotlin.String**| timezoneOffset | [optional]
 **userName** | **kotlin.String**| userName | [optional]

### Return type

[**OrderDetailIListInfoModelWrapper**](OrderDetailIListInfoModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getDefaultOrderTaskListUsingGET"></a>
# **getDefaultOrderTaskListUsingGET**
> kotlin.Array&lt;OrderTaskModel&gt; getDefaultOrderTaskListUsingGET(timezoneOffset, orderId, orderType, quantity, receiverAddress, receiverDistrictId, receiverRegionId, senderAddress, senderDistrictId, senderRegionId, totalWeight, userName)

getDefaultOrderTaskList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
val orderId : kotlin.Long = 789 // kotlin.Long | orderId
val orderType : kotlin.Int = 56 // kotlin.Int | orderType
val quantity : kotlin.Int = 56 // kotlin.Int | quantity
val receiverAddress : kotlin.String = receiverAddress_example // kotlin.String | receiverAddress
val receiverDistrictId : kotlin.Int = 56 // kotlin.Int | receiverDistrictId
val receiverRegionId : kotlin.Int = 56 // kotlin.Int | receiverRegionId
val senderAddress : kotlin.String = senderAddress_example // kotlin.String | senderAddress
val senderDistrictId : kotlin.Int = 56 // kotlin.Int | senderDistrictId
val senderRegionId : kotlin.Int = 56 // kotlin.Int | senderRegionId
val totalWeight : kotlin.Double = 1.2 // kotlin.Double | totalWeight
val userName : kotlin.String = userName_example // kotlin.String | userName
try {
    val result : kotlin.Array<OrderTaskModel> = apiInstance.getDefaultOrderTaskListUsingGET(timezoneOffset, orderId, orderType, quantity, receiverAddress, receiverDistrictId, receiverRegionId, senderAddress, senderDistrictId, senderRegionId, totalWeight, userName)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getDefaultOrderTaskListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getDefaultOrderTaskListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **timezoneOffset** | **kotlin.String**| timezoneOffset |
 **orderId** | **kotlin.Long**| orderId | [optional]
 **orderType** | **kotlin.Int**| orderType | [optional]
 **quantity** | **kotlin.Int**| quantity | [optional]
 **receiverAddress** | **kotlin.String**| receiverAddress | [optional]
 **receiverDistrictId** | **kotlin.Int**| receiverDistrictId | [optional]
 **receiverRegionId** | **kotlin.Int**| receiverRegionId | [optional]
 **senderAddress** | **kotlin.String**| senderAddress | [optional]
 **senderDistrictId** | **kotlin.Int**| senderDistrictId | [optional]
 **senderRegionId** | **kotlin.Int**| senderRegionId | [optional]
 **totalWeight** | **kotlin.Double**| totalWeight | [optional]
 **userName** | **kotlin.String**| userName | [optional]

### Return type

[**kotlin.Array&lt;OrderTaskModel&gt;**](OrderTaskModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getOrderDetailList1UsingGET"></a>
# **getOrderDetailList1UsingGET**
> OrderDetailIListInfoModelWrapper getOrderDetailList1UsingGET(orderid, timezoneOffset)

getOrderDetailList1

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderid : kotlin.Long = 789 // kotlin.Long | orderid
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : OrderDetailIListInfoModelWrapper = apiInstance.getOrderDetailList1UsingGET(orderid, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getOrderDetailList1UsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getOrderDetailList1UsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderid** | **kotlin.Long**| orderid |
 **timezoneOffset** | **kotlin.String**| timezoneOffset | [optional]

### Return type

[**OrderDetailIListInfoModelWrapper**](OrderDetailIListInfoModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getOrderMemoUsingGET"></a>
# **getOrderMemoUsingGET**
> kotlin.Array&lt;OrderMemo&gt; getOrderMemoUsingGET(orderId, timezoneOffset)

getOrderMemo

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderId : kotlin.Long = 789 // kotlin.Long | orderId
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : kotlin.Array<OrderMemo> = apiInstance.getOrderMemoUsingGET(orderId, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getOrderMemoUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getOrderMemoUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **kotlin.Long**| orderId |
 **timezoneOffset** | **kotlin.String**| timezoneOffset | [optional]

### Return type

[**kotlin.Array&lt;OrderMemo&gt;**](OrderMemo.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getPaymentDetailsUsingGET"></a>
# **getPaymentDetailsUsingGET**
> PaymentDetailsModel getPaymentDetailsUsingGET(orderId, timezoneOffset)

getPaymentDetails

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderId : kotlin.String = orderId_example // kotlin.String | orderId
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : PaymentDetailsModel = apiInstance.getPaymentDetailsUsingGET(orderId, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getPaymentDetailsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getPaymentDetailsUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderId** | **kotlin.String**| orderId |
 **timezoneOffset** | **kotlin.String**| timezoneOffset |

### Return type

[**PaymentDetailsModel**](PaymentDetailsModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getSenderListUsingGET"></a>
# **getSenderListUsingGET**
> kotlin.Array&lt;OrderManagementModel&gt; getSenderListUsingGET()

getSenderList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
try {
    val result : kotlin.Array<OrderManagementModel> = apiInstance.getSenderListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getSenderListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getSenderListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;OrderManagementModel&gt;**](OrderManagementModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getUpdateUsingPOST"></a>
# **getUpdateUsingPOST**
> OrderDetailIListInfoModelWrapper getUpdateUsingPOST(orderDetailModel)

getUpdate

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderDetailModel : OrderDetailModelWrapper =  // OrderDetailModelWrapper | orderDetailModel
try {
    val result : OrderDetailIListInfoModelWrapper = apiInstance.getUpdateUsingPOST(orderDetailModel)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#getUpdateUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#getUpdateUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderDetailModel** | [**OrderDetailModelWrapper**](OrderDetailModelWrapper.md)| orderDetailModel |

### Return type

[**OrderDetailIListInfoModelWrapper**](OrderDetailIListInfoModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateOrderAndTaskStatusUsingPOST"></a>
# **updateOrderAndTaskStatusUsingPOST**
> kotlin.Array&lt;OrderSyncModel&gt; updateOrderAndTaskStatusUsingPOST(updateList)

updateOrderAndTaskStatus

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val updateList : kotlin.Array<OrderStatusManagementModel> =  // kotlin.Array<OrderStatusManagementModel> | updateList
try {
    val result : kotlin.Array<OrderSyncModel> = apiInstance.updateOrderAndTaskStatusUsingPOST(updateList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#updateOrderAndTaskStatusUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#updateOrderAndTaskStatusUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **updateList** | [**kotlin.Array&lt;OrderStatusManagementModel&gt;**](OrderStatusManagementModel.md)| updateList |

### Return type

[**kotlin.Array&lt;OrderSyncModel&gt;**](OrderSyncModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateOrderMemoUsingPOST"></a>
# **updateOrderMemoUsingPOST**
> kotlin.Array&lt;OrderMemo&gt; updateOrderMemoUsingPOST(orderMemoList, timezoneOffset)

updateOrderMemo

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val orderMemoList : kotlin.Array<OrderMemo> =  // kotlin.Array<OrderMemo> | orderMemoList
val timezoneOffset : kotlin.String = timezoneOffset_example // kotlin.String | timezoneOffset
try {
    val result : kotlin.Array<OrderMemo> = apiInstance.updateOrderMemoUsingPOST(orderMemoList, timezoneOffset)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#updateOrderMemoUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#updateOrderMemoUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderMemoList** | [**kotlin.Array&lt;OrderMemo&gt;**](OrderMemo.md)| orderMemoList |
 **timezoneOffset** | **kotlin.String**| timezoneOffset | [optional]

### Return type

[**kotlin.Array&lt;OrderMemo&gt;**](OrderMemo.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateOrdersUsingPOST"></a>
# **updateOrdersUsingPOST**
> OrderManagementUpdateModelWrapper updateOrdersUsingPOST(updateModel)

updateOrders

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val updateModel : OrderManagementModelWrapper =  // OrderManagementModelWrapper | updateModel
try {
    val result : OrderManagementUpdateModelWrapper = apiInstance.updateOrdersUsingPOST(updateModel)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#updateOrdersUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#updateOrdersUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **updateModel** | [**OrderManagementModelWrapper**](OrderManagementModelWrapper.md)| updateModel |

### Return type

[**OrderManagementUpdateModelWrapper**](OrderManagementUpdateModelWrapper.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updatePaymentDetailsUsingPOST"></a>
# **updatePaymentDetailsUsingPOST**
> PaymentDetailsModel updatePaymentDetailsUsingPOST(paymentDetailsModel)

updatePaymentDetails

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = OrderManagementControllerApi()
val paymentDetailsModel : PaymentDetailsModel =  // PaymentDetailsModel | paymentDetailsModel
try {
    val result : PaymentDetailsModel = apiInstance.updatePaymentDetailsUsingPOST(paymentDetailsModel)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OrderManagementControllerApi#updatePaymentDetailsUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OrderManagementControllerApi#updatePaymentDetailsUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **paymentDetailsModel** | [**PaymentDetailsModel**](PaymentDetailsModel.md)| paymentDetailsModel |

### Return type

[**PaymentDetailsModel**](PaymentDetailsModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

