/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018

 * Description
 * -----------------------------------------------------------------------------------

 * YMValidator Class : Responsible for validating inputs

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.utils.validator

import android.graphics.Bitmap
import java.io.File
import java.util.regex.Pattern

object YMValidator {

    /**
     * To check if bitmap is valid
     * @param paramToCheck bitmap
     * @return true or false
     */
    fun validateBitmap(paramToCheck: Bitmap): Boolean {
        return null == paramToCheck
    }

    /**
     * To check if file exist or not
     * @param pathTofile path to file
     * @return boolean value tue or false
     */
    fun isFileExist(pathTofile: String): Boolean {
        if ( !pathTofile.isEmpty() && File(pathTofile).exists()) {

            return true

        }
        return false

    }

    /**
     * To validate exception is not null and message size is greater than 0
     * @param exception Exception
     * @return boolean value tue or false
     */
    fun validateException(exception: Exception): Boolean {
        return ( exception.message != null && exception.message!!.length > 0)
    }

    /**
     * To check if directory exist or not
     * @param pathToDirectory path to file
     * @return boolean true or false
     */
    fun isDirectoryExist(pathToDir: String): Boolean {
        if ( !pathToDir.isEmpty()) {
            val file = File(pathToDir)
            if (file.exists() && file.isDirectory)
                return true

        }
        return false

    }

    /**
     * To check  email id is valid or not
     */
    fun isEmailValid(target: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

    }

    /**
     * To validate pattern in given input
     * @param pattern pattern to match with
     * @param matcher string to match
     * @return true or false based on result
     */
    fun isPatternValid(pattern: String, matcher: String): Boolean {
        val p = Pattern.compile(pattern)
        val m = p.matcher(matcher)
        return m.matches()
    }

}