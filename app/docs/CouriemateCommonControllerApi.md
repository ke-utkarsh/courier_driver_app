# CouriemateCommonControllerApi

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllDistrictListUsingGET**](CouriemateCommonControllerApi.md#getAllDistrictListUsingGET) | **GET** /common/getDistrictList | getAllDistrictList
[**getAllRegionListUsingGET**](CouriemateCommonControllerApi.md#getAllRegionListUsingGET) | **GET** /common/getRegionList | getAllRegionList
[**getCarrierListUsingGET**](CouriemateCommonControllerApi.md#getCarrierListUsingGET) | **GET** /common/getCarrierList | getCarrierList
[**getCodeMasterUsingGET**](CouriemateCommonControllerApi.md#getCodeMasterUsingGET) | **GET** /common/getCodeMaster | getCodeMaster
[**getCompanyDetailsUsingGET**](CouriemateCommonControllerApi.md#getCompanyDetailsUsingGET) | **GET** /common/getCompanyDetails | getCompanyDetails
[**getNotifcationsUsingGET**](CouriemateCommonControllerApi.md#getNotifcationsUsingGET) | **GET** /common/fromUtc | getNotifcations
[**getNotifcationsUsingGET1**](CouriemateCommonControllerApi.md#getNotifcationsUsingGET1) | **GET** /common/getNotifications | getNotifcations
[**getNotifcationsUsingPOST**](CouriemateCommonControllerApi.md#getNotifcationsUsingPOST) | **POST** /common/testGet | getNotifcations
[**getNotifcationsUsingPOST1**](CouriemateCommonControllerApi.md#getNotifcationsUsingPOST1) | **POST** /common/toUtc | getNotifcations
[**getOrderNotifcationsUsingGET**](CouriemateCommonControllerApi.md#getOrderNotifcationsUsingGET) | **GET** /common/getOrderNotification | getOrderNotifcations
[**getOrderStatusListUsingGET**](CouriemateCommonControllerApi.md#getOrderStatusListUsingGET) | **GET** /common/getOrderStatusList | getOrderStatusList
[**getOrderTransitionStatusListUsingGET**](CouriemateCommonControllerApi.md#getOrderTransitionStatusListUsingGET) | **GET** /common/getOrderTransitionStatusList | getOrderTransitionStatusList
[**getOrderTypeListUsingGET**](CouriemateCommonControllerApi.md#getOrderTypeListUsingGET) | **GET** /common/getOrderTypeList | getOrderTypeList
[**getPaymentMethodsListUsingGET**](CouriemateCommonControllerApi.md#getPaymentMethodsListUsingGET) | **GET** /common/getPaymentMethodsList | getPaymentMethodsList
[**getRoleListUsingGET**](CouriemateCommonControllerApi.md#getRoleListUsingGET) | **GET** /common/getRoleList | getRoleList
[**getTaskStatusListUsingGET**](CouriemateCommonControllerApi.md#getTaskStatusListUsingGET) | **GET** /common/getTaskStatusList | getTaskStatusList
[**removeDeviceTokenUsingPOST**](CouriemateCommonControllerApi.md#removeDeviceTokenUsingPOST) | **POST** /common/removeDeviceToken | removeDeviceToken
[**storeDeviceTokenUsingPOST**](CouriemateCommonControllerApi.md#storeDeviceTokenUsingPOST) | **POST** /common/storeDeviceToken | storeDeviceToken


<a name="getAllDistrictListUsingGET"></a>
# **getAllDistrictListUsingGET**
> kotlin.Array&lt;DistrictMaster&gt; getAllDistrictListUsingGET()

getAllDistrictList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<DistrictMaster> = apiInstance.getAllDistrictListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getAllDistrictListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getAllDistrictListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;DistrictMaster&gt;**](DistrictMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAllRegionListUsingGET"></a>
# **getAllRegionListUsingGET**
> kotlin.Array&lt;RegionMaster&gt; getAllRegionListUsingGET()

getAllRegionList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<RegionMaster> = apiInstance.getAllRegionListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getAllRegionListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getAllRegionListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;RegionMaster&gt;**](RegionMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCarrierListUsingGET"></a>
# **getCarrierListUsingGET**
> kotlin.Any getCarrierListUsingGET()

getCarrierList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Any = apiInstance.getCarrierListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getCarrierListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getCarrierListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Any**](kotlin.Any.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCodeMasterUsingGET"></a>
# **getCodeMasterUsingGET**
> CodeMaster getCodeMasterUsingGET(dataId)

getCodeMaster

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val dataId : kotlin.Int = 56 // kotlin.Int | dataId
try {
    val result : CodeMaster = apiInstance.getCodeMasterUsingGET(dataId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getCodeMasterUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getCodeMasterUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dataId** | **kotlin.Int**| dataId |

### Return type

[**CodeMaster**](CodeMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getCompanyDetailsUsingGET"></a>
# **getCompanyDetailsUsingGET**
> CompanyMaster getCompanyDetailsUsingGET()

getCompanyDetails

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : CompanyMaster = apiInstance.getCompanyDetailsUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getCompanyDetailsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getCompanyDetailsUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CompanyMaster**](CompanyMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNotifcationsUsingGET"></a>
# **getNotifcationsUsingGET**
> java.sql.Timestamp getNotifcationsUsingGET(ts, zoneId)

getNotifcations

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val ts : kotlin.String = ts_example // kotlin.String | ts
val zoneId : kotlin.String = zoneId_example // kotlin.String | zoneId
try {
    val result : java.sql.Timestamp = apiInstance.getNotifcationsUsingGET(ts, zoneId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getNotifcationsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getNotifcationsUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ts** | **kotlin.String**| ts | [optional]
 **zoneId** | **kotlin.String**| zoneId | [optional]

### Return type

[**java.sql.Timestamp**](java.sql.Timestamp.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNotifcationsUsingGET1"></a>
# **getNotifcationsUsingGET1**
> kotlin.Array&lt;OrderNotification&gt; getNotifcationsUsingGET1()

getNotifcations

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<OrderNotification> = apiInstance.getNotifcationsUsingGET1()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getNotifcationsUsingGET1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getNotifcationsUsingGET1")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;OrderNotification&gt;**](OrderNotification.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNotifcationsUsingPOST"></a>
# **getNotifcationsUsingPOST**
> kotlin.collections.Map&lt;kotlin.String, java.sql.Timestamp&gt; getNotifcationsUsingPOST(date)

getNotifcations

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val date : kotlin.String = date_example // kotlin.String | date
try {
    val result : kotlin.collections.Map<kotlin.String, java.sql.Timestamp> = apiInstance.getNotifcationsUsingPOST(date)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getNotifcationsUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getNotifcationsUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **date** | **kotlin.String**| date | [optional]

### Return type

[**kotlin.collections.Map&lt;kotlin.String, java.sql.Timestamp&gt;**](java.sql.Timestamp.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getNotifcationsUsingPOST1"></a>
# **getNotifcationsUsingPOST1**
> kotlin.collections.Map&lt;kotlin.String, java.sql.Timestamp&gt; getNotifcationsUsingPOST1(model)

getNotifcations

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val model : AppDataModel =  // AppDataModel | model
try {
    val result : kotlin.collections.Map<kotlin.String, java.sql.Timestamp> = apiInstance.getNotifcationsUsingPOST1(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getNotifcationsUsingPOST1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getNotifcationsUsingPOST1")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**AppDataModel**](AppDataModel.md)| model |

### Return type

[**kotlin.collections.Map&lt;kotlin.String, java.sql.Timestamp&gt;**](java.sql.Timestamp.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOrderNotifcationsUsingGET"></a>
# **getOrderNotifcationsUsingGET**
> kotlin.Array&lt;OrderNotificationModel&gt; getOrderNotifcationsUsingGET(category, orderNo, receivedOn)

getOrderNotifcations

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val category : kotlin.Int = 56 // kotlin.Int | category
val orderNo : kotlin.String = orderNo_example // kotlin.String | orderNo
val receivedOn : kotlin.String = receivedOn_example // kotlin.String | receivedOn
try {
    val result : kotlin.Array<OrderNotificationModel> = apiInstance.getOrderNotifcationsUsingGET(category, orderNo, receivedOn)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getOrderNotifcationsUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getOrderNotifcationsUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **category** | **kotlin.Int**| category | [optional]
 **orderNo** | **kotlin.String**| orderNo | [optional]
 **receivedOn** | **kotlin.String**| receivedOn | [optional]

### Return type

[**kotlin.Array&lt;OrderNotificationModel&gt;**](OrderNotificationModel.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getOrderStatusListUsingGET"></a>
# **getOrderStatusListUsingGET**
> kotlin.Array&lt;OrderStatusMaster&gt; getOrderStatusListUsingGET()

getOrderStatusList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<OrderStatusMaster> = apiInstance.getOrderStatusListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getOrderStatusListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getOrderStatusListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;OrderStatusMaster&gt;**](OrderStatusMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getOrderTransitionStatusListUsingGET"></a>
# **getOrderTransitionStatusListUsingGET**
> kotlin.Array&lt;kotlin.Int&gt; getOrderTransitionStatusListUsingGET(orderStatusId)

getOrderTransitionStatusList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val orderStatusId : kotlin.Int = 56 // kotlin.Int | orderStatusId
try {
    val result : kotlin.Array<kotlin.Int> = apiInstance.getOrderTransitionStatusListUsingGET(orderStatusId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getOrderTransitionStatusListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getOrderTransitionStatusListUsingGET")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **orderStatusId** | **kotlin.Int**| orderStatusId | [optional]

### Return type

**kotlin.Array&lt;kotlin.Int&gt;**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getOrderTypeListUsingGET"></a>
# **getOrderTypeListUsingGET**
> kotlin.Array&lt;OrderType&gt; getOrderTypeListUsingGET()

getOrderTypeList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<OrderType> = apiInstance.getOrderTypeListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getOrderTypeListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getOrderTypeListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;OrderType&gt;**](OrderType.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getPaymentMethodsListUsingGET"></a>
# **getPaymentMethodsListUsingGET**
> kotlin.Array&lt;PaymentMethods&gt; getPaymentMethodsListUsingGET()

getPaymentMethodsList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<PaymentMethods> = apiInstance.getPaymentMethodsListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getPaymentMethodsListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getPaymentMethodsListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;PaymentMethods&gt;**](PaymentMethods.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getRoleListUsingGET"></a>
# **getRoleListUsingGET**
> kotlin.Array&lt;RoleMaster&gt; getRoleListUsingGET()

getRoleList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<RoleMaster> = apiInstance.getRoleListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getRoleListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getRoleListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;RoleMaster&gt;**](RoleMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTaskStatusListUsingGET"></a>
# **getTaskStatusListUsingGET**
> kotlin.Array&lt;TaskStatusMaster&gt; getTaskStatusListUsingGET()

getTaskStatusList

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
try {
    val result : kotlin.Array<TaskStatusMaster> = apiInstance.getTaskStatusListUsingGET()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#getTaskStatusListUsingGET")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#getTaskStatusListUsingGET")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Array&lt;TaskStatusMaster&gt;**](TaskStatusMaster.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="removeDeviceTokenUsingPOST"></a>
# **removeDeviceTokenUsingPOST**
> kotlin.Int removeDeviceTokenUsingPOST(model)

removeDeviceToken

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val model : UserTokenMapping =  // UserTokenMapping | model
try {
    val result : kotlin.Int = apiInstance.removeDeviceTokenUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#removeDeviceTokenUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#removeDeviceTokenUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**UserTokenMapping**](UserTokenMapping.md)| model |

### Return type

**kotlin.Int**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="storeDeviceTokenUsingPOST"></a>
# **storeDeviceTokenUsingPOST**
> kotlin.Int storeDeviceTokenUsingPOST(model)

storeDeviceToken

### Example
```kotlin
// Import classes:
//import io.swagger.client.infrastructure.*
//import io.swagger.client.models.*

val apiInstance = CouriemateCommonControllerApi()
val model : UserTokenMapping =  // UserTokenMapping | model
try {
    val result : kotlin.Int = apiInstance.storeDeviceTokenUsingPOST(model)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CouriemateCommonControllerApi#storeDeviceTokenUsingPOST")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CouriemateCommonControllerApi#storeDeviceTokenUsingPOST")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **model** | [**UserTokenMapping**](UserTokenMapping.md)| model |

### Return type

**kotlin.Int**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

