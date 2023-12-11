/**

 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 12, 2018
 * Description

 * -----------------------------------------------------------------------------------

 * FileOperations Class :  * File operations relates to the establish connection with File system in order
 * to Read/Write data from/to Internal or External storage memory of Android
 * device. This Class will handle all operations that are required to establish
 * connection with storage.

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description

 * -----------------------------------------------------------------------------------

 */
package `in`.ymsli.ymlibrary.utils


import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.errors.YMErrorCode
import `in`.ymsli.ymlibrary.errors.YMExceptions
import `in`.ymsli.ymlibrary.utils.validator.YMValidator
import android.content.Context
import android.util.Log
import java.io.*
import java.util.*


object FileOperations {

    /**
     * @param path path to file
     * @param context instance of Context
     * @return true or false
     *
     */

    fun deleteFile(path: String): Boolean {
        var isFileDeleted = false
        // Validate file path
        try {
            validatePath(path)

        val file = File(path)

        // Delete file
        if (file.exists()) {
            isFileDeleted = file.delete()

        }
        }
        catch (exception: YMExceptions){
        CommonUtils.showLog(Log.ERROR,exception.errorMessage)
        }
        return isFileDeleted
    }

    /**
     * @param pathArray array of files path
     *
     */
    @Throws(YMExceptions::class)
    fun deleteMultipleFiles(pathArray: Array<String>) {
        for (filePath in pathArray) {

            val file = File(filePath)
            // Delete image
            if (file.exists()) {
                file.delete()
            }

            // Notify media scan to remove images immediately from SD Card

        }
    }

    /**
     * @param dirFile directory file to be deleted
     * @param deleteSubFolders boolean value for requirement of deleting subfolders
     * @param context instance of context
     */
    fun deleteDirectory(dirFile: File, deleteSubFolders: Boolean, context: Context) {
        // Get List of files inside this directory
        val listFile = dirFile.listFiles()

        if (listFile != null) {
            for (aListFile in listFile) {
                if (aListFile.isDirectory) {
                    if (deleteSubFolders) {
                        // Delete files inside the folder
                        deleteDirectory(aListFile, true, context)
                        aListFile.delete()
                    }
                } else {
                    aListFile.delete()


                    // Delete thumbnail as well

                }
            }
        }
    }

    /**
     * Deletes a non-empty directory
     * @param dir   Directory file
     * @param context Context
     * @return true, if files deleted else false
     */
    fun deleteDirectory(dir: File, context: Context): Boolean {

        if (dir != null && dir.exists()) {
            val files = dir.listFiles() ?: return true

            for (file in files) {
                if (file.isDirectory) {
                    deleteDirectory(file, context)

                } else {
                    file.delete()


                }
            }
        }

        return dir.delete()
    }


    /**
     * Validate path
     * @param path
     *
     */

    private fun validatePath(path: String) {
        if (StringUtils.isNullOrEmptyAfterTrim(path)) {
            throw YMExceptions(YMErrorCode.PATH_FORMAT_WRONG, "")
        }

        // Check whether file exists or not at given path
        if (!YMValidator.isFileExist(path)) {
            throw YMExceptions(YMErrorCode.PATH_NOT_EXISTS,
                    "")
        }
    }


    /**
     * Writes string data into a specified file with the extension.
     * @param dataToWrite data to write in String form
     * @param filePath   file path
     */
    fun writeTextToFile(dataToWrite: String, filePath: String) {
        var out: BufferedWriter? = null
        var fileWriter: FileWriter? = null

        try {
            val destinationFile = File(filePath)
            if (!destinationFile.parentFile.exists()) {
                destinationFile.parentFile.mkdirs()
            }
            fileWriter = FileWriter(filePath)
            out = BufferedWriter(fileWriter)

            out.write(dataToWrite)
        } catch (fileNotFoundException: FileNotFoundException) {
            CommonUtils.showLog(Log.ERROR, fileNotFoundException.message)
        } catch (ioExp: IOException) {
            CommonUtils.showLog(Log.ERROR, ioExp.message)
        } finally {
            try {
                if (null != out) {
                    out.close()
                }
                if (null != fileWriter) {
                    fileWriter.close()
                }
            } catch (ioExp: IOException) {
                CommonUtils.showLog(Log.ERROR, ioExp.message)
            }

        }
    }

    /**
     * This method copies the src file to dest file.
     * @param srcFile   src file
     * @param destFile  destintion file
     * @throws IOException
     */
    @Throws(YMExceptions::class)
    fun copyFile(srcFile: File, destFile: File) {
        if (srcFile.exists()) {
            val input = FileInputStream(srcFile)
            // Check if destination file's parent directory exist, if not, try creating it, if unsuccessful throw an exception.
            val parentFile = destFile.parentFile
            if (!parentFile!!.exists()) {
                parentFile.mkdirs()
            }
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                input.close()
                throw YMExceptions(YMErrorCode.PARENT_DIRETORY_INVALID, YMConstants.EMPTY_STRING)
            }
            val out = FileOutputStream(destFile)
            val buf = ByteArray(1024)
            var len = input.read(buf)
            while (len > 0) {
                out.write(buf, 0, len)
                len = input.read(buf)
            }
            input.close()
            out.close()

        }
    }


    /**
     * This method will create given file
     * @param destFile destination file
     */


    fun createFile(destFile: File) {
        val parentFile = destFile.parentFile
        if (!parentFile!!.exists()) {
            parentFile.mkdirs()
        }
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw YMExceptions(YMErrorCode.PARENT_DIRETORY_INVALID, YMConstants.EMPTY_STRING)
        }

        else
            try {
                destFile.createNewFile()
            }
            catch (exception:IOException){
                CommonUtils.showLog(Log.ERROR,exception.message)
                     //   throw YMExceptions(YMErrorCode.IO_EXCEPTION, YMConstants.EMPTY_STRING)
            }

    }

    /**
     * Get Name of all folders at given path
     * @param path path of directory
     * @return List with name of all folders
     */
    fun getAllFolderNameFromPath(path: String): ArrayList<String>? {
        val file = File(path)
        var directoryList: ArrayList<String>? = null
        try {

            val allFile = file.listFiles()
            if (null != allFile) {
                directoryList = ArrayList()

                for (eachFile in allFile) {
                    if (eachFile.isDirectory) {
                        directoryList.add(eachFile.name)
                    }
                }
            }
        } catch (filenotFoundExcption: FileNotFoundException) {
            CommonUtils.showLog(Log.ERROR, filenotFoundExcption.message)
          //  throw YMExceptions(YMErrorCode.FILE_NOT_FOUND, YMConstants.EMPTY_STRING)
        }

        return directoryList
    }

    /**
     * Get path of all sub folders at given path
     * @param path  path of directory
     * @param isReturnEmptyFolder return path of empty folder if true
     * @return List with path of all folders
     */

    fun getAllFolderPathFromDirPath(path: String, isReturnEmptyFolder: Boolean): ArrayList<String>? {
        var directoryList: ArrayList<String>? = null
        try {
            val file = File(path)

            val allFile = file.listFiles()
            if (null != allFile) {
                directoryList = ArrayList()

                for (eachFile in allFile) {
                    if (eachFile.isDirectory && (eachFile
                                    .listFiles().isNotEmpty() || isReturnEmptyFolder)) {
                        directoryList.add(eachFile.path)
                    }
                }
            }
        } catch (filenotFounExcption: FileNotFoundException) {
            CommonUtils.showLog(Log.ERROR, filenotFounExcption.message)
          //  throw YMExceptions(YMErrorCode.FILE_NOT_FOUND, YMConstants.EMPTY_STRING)
        }
        return directoryList
    }

    /**
     * Get paths of specific file type in given directory and its sub-directories by given path
     * @param directoryPath path of directory
     * @param fileType      File Extension
     * @return List of all paths
     */

    fun getFilePathsInDirectory(directoryPath: String, fileType: String): ArrayList<String> {
        val filePathArrayList = ArrayList<String>()
        try {


            // Validate path and File Type
            if (!StringUtils.isNullOrEmptyAfterTrim(fileType)) {
                if (YMValidator.isDirectoryExist(directoryPath)) {
                    val directory = File(directoryPath)

                    // Get all the files from a directory
                    val fList = directory.listFiles()

                    for (file in fList) {
                        if (file.isFile) {
                            if (file.name.endsWith(fileType)) {
                                // Add to Array list
                                filePathArrayList.add(file.absolutePath)
                            }
                        } else if (file.isDirectory) {

                            getFilePathsInDirectory(file.absolutePath, fileType)
                        }
                    }
                }
            }
        } catch (filenotFounExcption: FileNotFoundException) {
            CommonUtils.showLog(Log.ERROR, filenotFounExcption.message)
            //throw YMExceptions(YMErrorCode.FILE_NOT_FOUND, YMConstants.EMPTY_STRING)
        }
        return filePathArrayList
    }

    /**
     * Get paths of specific file type in given directory and its sub-directories by given path
     * @param directoryPath path of directory
     * @param fileType      File Extension
     * @return List of all paths
     */

    fun getFilePathsInDirectoryRecusrsive(list: ArrayList<String>?, directoryPath: String, fileType: String): ArrayList<String> {
        var filePathArrayList: ArrayList<String> = ArrayList()
        try {

            if (list!= null)

                filePathArrayList = list

            // Validate path and File Type
            if (!StringUtils.isNullOrEmptyAfterTrim(directoryPath) && !StringUtils.isNullOrEmptyAfterTrim(fileType)) {
                if (YMValidator.isDirectoryExist(directoryPath)) {
                    val directory = File(directoryPath)

                    // Get all the files from a directory
                    val fList = directory.listFiles()

                    for (file in fList) {
                        if (file.isFile) {
                            if (file.name.endsWith(fileType)) {
                                // Add to Array list
                                filePathArrayList.add(file.absolutePath)
                            }
                        } else if (file.isDirectory) {
                            getFilePathsInDirectoryRecusrsive(filePathArrayList, file.absolutePath, fileType)
                        }
                    }
                }
            }
        } catch (filenotFounExcption: FileNotFoundException) {
            CommonUtils.showLog(Log.ERROR, filenotFounExcption.message)
           // throw YMExceptions(YMErrorCode.FILE_NOT_FOUND, YMConstants.EMPTY_STRING)
        }
        return filePathArrayList
    }


    /**
     * Get size of directory
     * @param dirFile Directory File
     * @return
     */
    fun getFolderSize(dirFile: File?): Long {
        var size: Long = 0

        if (dirFile != null) {
            if (dirFile.isDirectory) {
                for (file in dirFile.listFiles()) {
                    size += getFolderSize(file)
                }
            } else {
                size = getFileSize(dirFile)
            }
        }

        return size
    }

    /**
     * Get size of file
     * @param file File
     * @return
     */
    fun getFileSize(file: File?): Long {
        var size: Long = 0

        if (file != null && file.isFile) {
            size = file.length()
        }

        return size
    }

    /**
     * Get creation time for the file
     * @param filePath File path
     * @return creation time for the file
     */
    fun getFileCreationTime(filePath: String): Long {
        val file = File(filePath)
        val lastModDate = Date(file.lastModified())

        return lastModDate.time
    }


}