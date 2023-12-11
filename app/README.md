# io.swagger.client - Kotlin client library for couriermate-API

## Requires

* Kotlin 1.1.2
* Gradle 3.3

## Build

First, create the gradle wrapper script:

```
gradle wrapper
```

Then, run:

```
./gradlew check assemble
```

This runs all tests and packages the library.

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in Swagger definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://10.167.23.72:5000/couriermate-api*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ApiDetailControllerApi* | [**getApiConfigurationUsingGET**](docs/ApiDetailControllerApi.md#getapiconfigurationusingget) | **GET** /apiConfig/getApiConfiguration | getApiConfiguration
*AwsMqttControllerApi* | [**pushMessageToAWSMQTTUsingPOST**](docs/AwsMqttControllerApi.md#pushmessagetoawsmqttusingpost) | **POST** /aws/pushMessageToAWSMQTT | pushMessageToAWSMQTT
*CacheControllerApi* | [**evictCacheUsingPOST**](docs/CacheControllerApi.md#evictcacheusingpost) | **POST** /cache/evict | evictCache
*LogbackControllerApi* | [**disableDbLoggingUsingGET**](docs/LogbackControllerApi.md#disabledbloggingusingget) | **GET** /logback/disableDbLogging | disableDbLogging
*LogbackControllerApi* | [**enableDbLoggingUsingGET**](docs/LogbackControllerApi.md#enabledbloggingusingget) | **GET** /logback/enableDbLogging | enableDbLogging
*LoginControllerApi* | [**authenticateUserUsingPOST**](docs/LoginControllerApi.md#authenticateuserusingpost) | **POST** /login | authenticateUser
*NotificationControllerApi* | [**sendFcmNotificationToSingleDeviceUsingPOST**](docs/NotificationControllerApi.md#sendfcmnotificationtosingledeviceusingpost) | **POST** /notification/sendFcmNotificationToSingleDevice | sendFcmNotificationToSingleDevice
*NotificationControllerApi* | [**sendFcmNotificationToTopicUsingPOST**](docs/NotificationControllerApi.md#sendfcmnotificationtotopicusingpost) | **POST** /notification/sendFcmNotificationToTopic | sendFcmNotificationToTopic
*UserControllerApi* | [**changePasswordUsingPUT1**](docs/UserControllerApi.md#changepasswordusingput1) | **PUT** /user/changePassword | changePassword
*UserControllerApi* | [**createUserUsingPOST**](docs/UserControllerApi.md#createuserusingpost) | **POST** /user/create | createUser
*UserControllerApi* | [**deleteUserUsingDELETE**](docs/UserControllerApi.md#deleteuserusingdelete) | **DELETE** /user/delete/{id} | deleteUser
*UserControllerApi* | [**getPortalUsersUsingGET**](docs/UserControllerApi.md#getportalusersusingget) | **GET** /user/getUsers | getPortalUsers
*UserControllerApi* | [**updateUserUsingPUT**](docs/UserControllerApi.md#updateuserusingput) | **PUT** /user/update | updateUser


<a name="documentation-for-models"></a>
## Documentation for Models

 - [io.swagger.client.models.APIConfigurationDTO](docs/APIConfigurationDTO.md)
 - [io.swagger.client.models.APIResponse](docs/APIResponse.md)
 - [io.swagger.client.models.ChangePasswordDTO](docs/ChangePasswordDTO.md)
 - [io.swagger.client.models.FCMData](docs/FCMData.md)
 - [io.swagger.client.models.FCMNotification](docs/FCMNotification.md)
 - [io.swagger.client.models.FCMNotificationRequestForSingleDevice](docs/FCMNotificationRequestForSingleDevice.md)
 - [io.swagger.client.models.FCMNotificationRequestForTopic](docs/FCMNotificationRequestForTopic.md)
 - [com.ymsli.couriemate.model.UserMaster](docs/UserMaster.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="JWT"></a>
### JWT

- **Type**: API key
- **API key parameter name**: Authorization
- **Location**: HTTP header

