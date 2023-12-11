package ymsli.com.couriemate.utils.services

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author VE00YM129
 * @date   December 5, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ZipUploadService : This JobService is responsible for file creation or edit existing
 *          files. The data stored is the location data in DAPIoT format.
 *           It also sends files to DAPIoT server
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.app.job.JobParameters
import android.os.Build
import android.util.Log
import ir.mahdi.mzip.zip.ZipArchive
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import ymsli.com.couriemate.BuildConfig
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseJobService
import ymsli.com.couriemate.database.entity.AccelerometerEntity
import ymsli.com.couriemate.database.entity.DAPIoTFileEntity
import ymsli.com.couriemate.database.entity.GyroEntity
import ymsli.com.couriemate.database.entity.LatLongEntity
import ymsli.com.couriemate.di.component.ServiceComponent
import ymsli.com.couriemate.utils.common.Constants
import ymsli.com.couriemate.utils.common.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ZipUploadService: BaseJobService() {

    companion object{
        const val STDD_FILE_TYPE = 1
        const val GPSD_FILE_TYPE = 2
        private const val JSON_SUFFIX = ".json"
        private const val ZIP_SUFFIX = ".zip"
    }

    private lateinit var fileNameTime: String
    private var ccuId: String? = null

    //json parameters
    private lateinit var accelJsonArray: JSONArray
    private lateinit var gyroJsonArray: JSONArray
    private lateinit var locationJsonArray: JSONArray

    //json file upload parameters
    var xuid: String? = null

    //list of sensor and gps data
    private lateinit var gyroList: Array<GyroEntity>
    private lateinit var accelList: Array<AccelerometerEntity>
    private lateinit var trips: Array<LatLongEntity>

    private var isOlderSyncFilePassed: Boolean = true

    override fun inject(jobServiceComponent: ServiceComponent) = jobServiceComponent.inject(this)

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        /* check for Accel and Gyro data
           If any of the above is present,create a file
           and push it to the server */
        ccuId = couriemateRepository.getCCUID()
        if(ccuId.isNullOrBlank()) return true
     /*   createGyroJSONArray()
        createAccelJSONArray()*/

        /* check for location data If present,
           create file and push it to the server */
        createTripsJSONArray()
       /* if(createSTDDJSON()){
            cleanSensorTables() //delete sensor values which are added in file
        }*/

        if(createGPSDJSON()){
            updateLocationTable()// update isFileCreated to true
        }
        else{
            var doesAnyFileExist = false
            //executed when files are stuck in device
            val unsyncedDapFiles = couriemateRepository.getUnsyncedFiles()
            for(file in unsyncedDapFiles){
                if(isOlderSyncFilePassed) {//checks if previous file was synced only then it tries for other files
                    xuid = file.fileName.split("_")[0]
                    val canSent = file.nextTry ?: 0 < Utils.getTimeInMilliSec() && !file.isSent
                    val pendingFile = File(file.filePath)
                    if (canSent && pendingFile.exists()) {
                        doesAnyFileExist = true
                        syncFiles(pendingFile, pendingFile.name)
                    } else {
                        couriemateRepository.removeFileEntry(pendingFile.name)
                    }
                }
                else{
                    stopSelf()//stop the service if sync of files failed
                }
            }
            if(!doesAnyFileExist){
                // clear gyro and accel table
                couriemateRepository.clearGyroTable()
                couriemateRepository.clearAccelTable()
            }
        }
        return true
    }

    /** creates json array of Gyro sensor data */
    private fun createGyroJSONArray(){
        gyroJsonArray = JSONArray()
        gyroList = couriemateRepository.getUnsyncedGyroEntity()
        if(gyroList.isNotEmpty()){
            for(gyroEntity in gyroList){
                if(gyroEntity.x.isNotBlank() && //part of dap file validation
                    gyroEntity.y.isNotBlank() &&
                    gyroEntity.z.isNotBlank() &&
                    gyroEntity.tripId.isNotBlank() &&
                    gyroEntity.sensorTime.isNotBlank()) {
                    //if(ccuId.isNullOrBlank()) ccuId = couriemateRepository.getCCUIDForTrip(gyroEntity.tripId)
                    if(ccuId.isNullOrBlank()){
                        try {
                            //FirebaseCrashlytics.getInstance().recordException(Exception("CCUID is null"))
                        }
                        catch (ex: java.lang.Exception){

                        }
                    }
                    val gyroJSON = JSONObject()
                    val gyroContentJSON = JSONObject()
                    gyroContentJSON.put(application.resources.getString(R.string.sensor_x_key), gyroEntity.x)//content json object creation
                    gyroContentJSON.put(application.resources.getString(R.string.sensor_y_key), gyroEntity.y)
                    gyroContentJSON.put(application.resources.getString(R.string.sensor_z_key), gyroEntity.z)
                    gyroJSON.put(application.resources.getString(R.string.contents_key), gyroContentJSON)
                    //gyroJSON.put(application.resources.getString(R.string.join_key_key), application.resources.getString(R.string.join_key_value))
                    gyroJSON.put(application.resources.getString(R.string.dap_iot_trip_key), gyroEntity.tripId)
                    gyroJSON.put(application.resources.getString(R.string.timestamp_key), gyroEntity.sensorTime)
                    gyroJsonArray.put(gyroJSON)
                }
            }
        }
        else{
            // create empty JSON Array for GYRO
            gyroJsonArray.put(JSONObject())
        }
    }

    /** creates json array of Accelerometer sensor data */
    private fun createAccelJSONArray(){
        accelJsonArray = JSONArray()
        accelList = couriemateRepository.getUnsyncedAccelEntity()
        if(accelList.isNotEmpty()){
            for(accelEntity in accelList){
                if(accelEntity.x.isNotBlank() &&//part of dap file validation
                    accelEntity.y.isNotBlank() &&
                    accelEntity.z.isNotBlank() &&
                    accelEntity.tripId.isNotBlank() &&
                    accelEntity.sensorTime.isNotBlank()) {
                    //if (ccuId.isNullOrBlank()) ccuId = couriemateRepository.getCCUIDForTrip(accelEntity.tripId)
                    if(ccuId.isNullOrBlank()){
                        try {
                            //FirebaseCrashlytics.getInstance().recordException(Exception("CCUID is null"))
                        }
                        catch (ex: java.lang.Exception){

                        }
                    }
                    val accelJSON = JSONObject()
                    val accelContentJSON = JSONObject()
                    accelContentJSON.put(application.resources.getString(R.string.sensor_x_key), accelEntity.x) //content json creation
                    accelContentJSON.put(application.resources.getString(R.string.sensor_y_key), accelEntity.y)
                    accelContentJSON.put(application.resources.getString(R.string.sensor_z_key), accelEntity.z)
                    accelJSON.put(application.resources.getString(R.string.contents_key), accelContentJSON)
                    //accelJSON.put(application.resources.getString(R.string.join_key_key), application.resources.getString(R.string.join_key_value))
                    accelJSON.put(application.resources.getString(R.string.dap_iot_trip_key), accelEntity.tripId)
                    accelJSON.put(application.resources.getString(R.string.timestamp_key), accelEntity.sensorTime)
                    accelJsonArray.put(accelJSON)
                }
            }
        }
        else{
            accelJsonArray.put(JSONObject())
        }
    }

    /** creates json array of GPS location data */
    private fun createTripsJSONArray(){
        locationJsonArray = JSONArray()
        trips = couriemateRepository.getUnsyncedDAPTrips()
        if(trips.isNotEmpty()){
            for(location in trips){
                //part of dap file validation
                if(location.tripId.isNotBlank() && location.latitude.isNotBlank() && location.longitude.isNotBlank() && location.locationCaptureTime.isNotBlank()) {
                    val tripJson = JSONObject()
                    tripJson.put(application.resources.getString(R.string.contents_key), "${location.longitude},${location.latitude}") //gpsd json array creation
                    tripJson.put(application.resources.getString(R.string.join_key_key), application.resources.getString(R.string.join_key_value))
                    tripJson.put(application.resources.getString(R.string.dap_iot_trip_key), location.tripId)
                    tripJson.put(application.resources.getString(R.string.timestamp_key), location.locationCaptureTime)
                    locationJsonArray.put(tripJson)
                }
            }
        }
    }

    /** this is responsbile for creating STDD JSON */
    private fun createSTDDJSON(): Boolean{
        /* check accelJOSN Array and gyro json array size
           if size of either of them is above 0
           file will be pushed to server */
        if(accelJsonArray.length() > 0 || gyroJsonArray.length()>0 && !ccuId.isNullOrBlank()){//part of dap file validation
            val stddJSON = JSONObject()
            val gigyaId = couriemateRepository.getCCUID()
            if(gigyaId.isNullOrBlank()) return false

            val osVersion = "${application.resources.getString(R.string.os_name_value)} ${Build.VERSION.RELEASE}"
            stddJSON.put(application.resources.getString(R.string.os_info_key),osVersion)

            val appVersion = "${application.resources.getString(R.string.app_name_value)} ${BuildConfig.VERSION_NAME}"
            stddJSON.put(application.resources.getString(R.string.app_info_key), appVersion)

            fileNameTime = Utils.getTimeInDAPIoTFormat()
            stddJSON.put(application.resources.getString(R.string.timestamp_key),fileNameTime)
            stddJSON.put(application.resources.getString(R.string.gigya_uuid_key), gigyaId)
            stddJSON.put(application.resources.getString(R.string.user_id_key), gigyaId)

            val projectId = application.resources.getString(R.string.project_id_dapiot_value)
            stddJSON.put(application.resources.getString(R.string.device_type_key), projectId)

            stddJSON.put(application.resources.getString(R.string.ccu_id_key), "${projectId}${ccuId}")

            if(accelList.isEmpty() && gyroList.isEmpty()) return false

            if(accelList.size>0) stddJSON.put(application.resources.getString(R.string.accelerometer_key), accelJsonArray)
            if(gyroList.size>0) stddJSON.put(application.resources.getString(R.string.gyro_key), gyroJsonArray)

            //validate above values and return false if any validation failed
            if(osVersion.isBlank() || appVersion.isBlank() ||//part of dap file validation
                fileNameTime.isBlank() || gigyaId.isBlank() || projectId.isBlank()){
                return false
            }

            // CREATE STDD JSON and ZIP file and push to server
            return createJSONFile(stddJSON.toString(),STDD_FILE_TYPE)
        }
        return false
    }

    /**
     * creates complete GPSD JSON
     */
    private fun createGPSDJSON(): Boolean{
        val gigyaId = couriemateRepository.getCCUID()
        if(gigyaId.isNullOrBlank()) return false
        if(locationJsonArray.length()>0 && !ccuId.isNullOrBlank()){//part of dap file validation
            val gpsdJson = JSONObject()

            val osVersion = "${application.resources.getString(R.string.os_name_value)} ${Build.VERSION.RELEASE}"
            gpsdJson.put(application.resources.getString(R.string.os_info_key), osVersion)

            val appVersion = "${application.resources.getString(R.string.app_name_value)} ${BuildConfig.VERSION_NAME}"
            gpsdJson.put(application.resources.getString(R.string.app_info_key), appVersion)

            fileNameTime = Utils.getTimeInDAPIoTFormat()
            gpsdJson.put(application.resources.getString(R.string.timestamp_key),fileNameTime)

            gpsdJson.put(application.resources.getString(R.string.gigya_uuid_key), gigyaId)
            gpsdJson.put(application.resources.getString(R.string.user_id_key), gigyaId)

            gpsdJson.put(application.resources.getString(R.string.acc_auth_key),true.toString())//as recommended in the document of DAPIoT, this will is true for Android

            val projectId = application.resources.getString(R.string.project_id_dapiot_value)//content json creation
            gpsdJson.put(application.resources.getString(R.string.device_type_key), projectId)
            gpsdJson.put(application.resources.getString(R.string.ccu_id_key), "${projectId}${ccuId}")
            gpsdJson.put(application.resources.getString(R.string.gps_key), locationJsonArray)

            //validate above values and return false if any validation failed
            if(osVersion.isBlank() || appVersion.isBlank() || fileNameTime.isBlank() || gigyaId.isBlank() || projectId.isBlank()){//part of dap file validation
                return false
            }

            // CREATE GPSD JSON and ZIP file and push to server
            return createJSONFile(gpsdJson.toString(), GPSD_FILE_TYPE)
        }
        return false
    }

    /**
     * creates JSON file for input JSON String
     * and corresponding ZIP file
     */
    private fun createJSONFile(fileContent: String,fileType: Int): Boolean{
        val directory = verifyDirectoryExistence()
        val fileName = getFileName(fileType)
        if(fileName==null || fileName.contains("null",true)) return false//part of dap file validation
        val file = File(directory, "$fileName$JSON_SUFFIX")
        try{
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(fileContent.toByteArray())//contents written in json file
            fos.flush()
            fos.close()

            // create ZIP file
            if(!fileName.isNullOrBlank() && file.exists()){
                xuid = fileName.split("_")[0]
                val password = passwordEnable()
                val name = fileName.removeSuffix(JSON_SUFFIX)
                ZipArchive.zip(file.absolutePath,"${file.parent}/${name}$ZIP_SUFFIX",password)//json file is compressed to zip file
                //store file info in Room
                val jsonFIle = File(file.parent + "/" + name + ZIP_SUFFIX)
                storeFileInfo(jsonFIle)

                //upload zip to DAPIoT server
                if(file.exists()) {
                    syncFiles(jsonFIle,name)
                    file.delete()//file is converted to zip and hence json file can be deleted in order to have low memory usage
                    return true
                }
            }
        }
        catch (e: IOException){
            Log.d("IOException",e.message)
            return false
        }
        return false
    }

    /**
     * responsible for uploading
     * GPSD and STDD files
     */
    private fun syncFiles(zipFolderGPSD: File,name: String){
        if(!zipFolderGPSD.exists()) return//part of dap file validation
        val fileRequestBody =
            RequestBody.create(MediaType.parse(Constants.REQUEST_MEDIA_TYPE_VALUE), zipFolderGPSD)
            //GlobalScope.launch(Dispatchers.IO) {
            if(!xuid.isNullOrBlank() && isNetworkConnected()){
                try {
                    //request preparation to upload ZIP file to DAP IoT server
                    compositeDisposable.addAll(
                        couriemateRepository.sentLocationDataToDAPIoTServer(xuid!!, "${zipFolderGPSD.name}", fileRequestBody)
                            .subscribeOn(schedulerProvider.io())
                            .subscribe({
                                if (!it.SequenceNumber.isNullOrBlank()) {
                                    //delete file and its entry from Room
                                    handleSuccessFileUpload(zipFolderGPSD.name)
                                    zipFolderGPSD.delete() // if file is uploaded to server, delete the file off the memory
                                    if (zipFolderGPSD.exists()) {
                                        zipFolderGPSD.delete()
                                    }
                                } else {
                                    isOlderSyncFilePassed = false
                                    handledFailedFileUpload(zipFolderGPSD.name)//handle case when file upload has failed
                                }
                            }, {
                                isOlderSyncFilePassed = false
                                handledFailedFileUpload(zipFolderGPSD.name)
                            })
                    )
                }
                catch (ex: Exception){
                    isOlderSyncFilePassed = false
                    handledFailedFileUpload(zipFolderGPSD.name)
                }
            }
        //}
    }

    /**
     * delete entry of uploaded file from room
     * and clears the pending queue of zip files
     */
    private fun handleSuccessFileUpload(fileName: String){
        couriemateRepository.removeFileEntry(fileName)
        // try sending other files too
        val unsyncedFiles = couriemateRepository.getUnsyncedFiles()
        if(unsyncedFiles.isNotEmpty()){
            for(unsyncedFile in unsyncedFiles){
                val canSent = unsyncedFile.nextTry?:0 < Utils.getTimeInMilliSec()
                unsyncedFile.filePath = unsyncedFile.filePath.replace(JSON_SUFFIX, ZIP_SUFFIX)
                val file = File(unsyncedFile.filePath)
                if(file.exists() && canSent){
                    xuid = file.name.split("_")[0]
                    syncFiles(file,file.name)
                }
            }
        }
    }

    /**
     * updates entry of failed file in room
     */
    private fun handledFailedFileUpload(fileName: String){
        val fileEntity = couriemateRepository.getFileEntity(fileName)
        if(fileEntity!=null && fileEntity.id!=null) {
            val numTries = fileEntity.retryAttempts
            if (numTries >= 4) {//if file upload fails, retry it after 5,10,20 & 40 minutes interval only
                fileEntity.retryAttempts = 0
                fileEntity.nextTry = Utils.getTimeInMilliSec()
            } else {
                fileEntity.retryAttempts = (fileEntity.retryAttempts ?: 0 + 1)
                val nextMinute =
                    5 * Math.pow(2.toDouble(), (fileEntity.retryAttempts - 1).toDouble())
                val nextTry = (Utils.getTimeInMilliSec() + (nextMinute * 60 * 1000))
                fileEntity.nextTry = nextTry.toLong()
            }
            if(fileEntity.nextTry!=null){
                couriemateRepository.updateFileEntity(fileEntity.id, fileEntity.nextTry!!,fileEntity.retryAttempts)
            }
        }
    }

    /**
     * generates the name of file based
     * on the type of file
     */
    private fun getFileName(fileType: Int): String?{
        if(fileType==STDD_FILE_TYPE){
            //file name for STDD file
            return "${application.resources.getString(R.string.project_id_dapiot_value)}${ccuId}${application.resources.getString(R.string.stdd_file_name_value)}${Utils.getTimeForFileName(fileNameTime)}"
        }
        else if(fileType == GPSD_FILE_TYPE){
            //file name for GPSD file
            return "${application.resources.getString(R.string.project_id_dapiot_value)}${ccuId}${application.resources.getString(R.string.gpsd_file_name_value)}${Utils.getTimeForFileName(fileNameTime)}"
        }
        return null
    }
    /**
     * makes directory if doesn't exists
     */
    private fun verifyDirectoryExistence(): File {
        val directory = File(this.filesDir, Constants.DIRECTORY)
        if (!directory.exists()) directory.mkdirs()
        return directory
    }

    /**
     * stores current generated file
     * info in room db
     */
    private fun storeFileInfo(file: File){
        //store file info in entity
        val dapIoTFileEntity = DAPIoTFileEntity(fileName = file.name, filePath = file.absolutePath, createdOn = Utils.getTimeInMilliSec(), retryAttempts = 0,isSent = false)
        couriemateRepository.storeFileInfo(dapIoTFileEntity)
    }

    /**
     * generates password for ZIP file
     */
    private fun passwordEnable(): String? {
        val ccuIdMod = "${xuid}${application.resources.getString(R.string.dap_iot_salt)}"
        val messageDigest: MessageDigest
        try {
            messageDigest = MessageDigest.getInstance(application.resources.getString(R.string.sha_256_value))
            messageDigest.update(ccuIdMod.toByteArray(StandardCharsets.UTF_8))
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
            return ""
        }
        val hex = StringBuilder()
        for (b in messageDigest.digest()) {
            hex.append(String.format("%02x",b.toInt() and 0xFF))
        }
        var password = hex.toString().substring(0, 16).toUpperCase()

        // convert alphabetic to lowercase
        password = replaceAtToSmallCharacterIfLetter(
            intArrayOf(3, 7, 11, 15),
            password
        )
        // convert alphabetic to "!"
        password = replaceAtToExclamationMarkIfLetter(
            intArrayOf(2, 6, 10, 14),
            password
        )
        // If it's a number, convert it to "-"
        password = replaceAtToHyphenIfLetter(
            intArrayOf(1, 5, 9, 13),
            password
        )
        return password
    }

    /**
     * interacts with password generation function
     */
    private fun replaceAtToSmallCharacterIfLetter(
        positionToChange: IntArray,
        word: String
    ): String {
        var word = word
        for (position in positionToChange) {
            if (Character.isLetter(word[position])) {
                word = replaceCharAt(
                    word, position,
                    Character.toLowerCase(word[position])
                )
            }
        }
        return word
    }

    /**
     * interacts with password generation function
     */
    private fun replaceAtToExclamationMarkIfLetter(
        positionToChange: IntArray,
        word: String
    ): String {
        var word = word
        for (position in positionToChange) {
            if (Character.isLetter(word[position])) {
                word = replaceCharAt(word, position, '!')
            }
        }
        return word
    }

    /**
     * interacts with password generation function
     */
    private fun replaceAtToHyphenIfLetter(
        positionToChange: IntArray,
        word: String
    ): String {
        var word = word
        for (position in positionToChange) {
            if (Character.isDigit(word[position])) {
                word = replaceCharAt(word, position, '-')
            }
        }
        return word
    }

    /**
     * interacts with password generation function
     */
    private fun replaceCharAt(
        stringToChange: String, position: Int,
        newChar: Char
    ): String {
        val charList = stringToChange.toCharArray()
        charList[position] = newChar
        return String(charList)
    }

    /**
     * delete sensor data whose file
     * has been created
     */
    private fun cleanSensorTables(){
        if(gyroList.isNotEmpty()){
            for(g in gyroList){
                if(g.id!=null){
                   couriemateRepository.removeSyncedGyroData(g.id)
                }
            }
        }

        if(accelList.isNotEmpty()){
            for(a in accelList){
                if(a.id!=null){
                    couriemateRepository.removeSyncedAccelData(a.id)
                }
            }
        }
    }

    /**
     * upadtes parameter in location table
     * which signifies that data for row
     * has been created.
     */
    private fun updateLocationTable(){
        if(trips.isNotEmpty()){
            for(t in trips){
                if(t.locationId!=null) {
                    couriemateRepository.updateTripParameter(t.locationId)
                }
            }
        }
    }
}