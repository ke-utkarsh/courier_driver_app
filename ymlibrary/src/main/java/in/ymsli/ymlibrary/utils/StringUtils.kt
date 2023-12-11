/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 12, 2018

 * Description
 * -----------------------------------------------------------------------------------

 * StringUtils Class : Responsible for providing string validations and string related operations

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.utils


object StringUtils {

    /**
     * Concatenates a number of Strings together in the form of a String.
     * @param strings [String] The strings to be concatenated.
     * @return concatenatedString [String] The finally concatenated string.
     */
    fun concatenate(vararg strings: String): String? {
        var concatenatedString: String? = null
        if (null != strings) {
            val concatenatedStringBuilder = StringBuilder()
            for (string in strings) {
                concatenatedStringBuilder.append(string)
            }
            concatenatedString = concatenatedStringBuilder.toString()
        }
        return concatenatedString
    }

    /**
     * Checks if a string is null or empty.
     * @param paramToCheck [String] String to check for empty or null
     * @return boolean, true if paramToCheck is null or empty.
     */
    fun isNullOrEmpty(paramToCheck: String?): Boolean {
        return null == paramToCheck || paramToCheck.isEmpty()
    }

    /**
     * Checks if a string is null or empty after trimming
     * @param paramToCheck [String] String to check for empty or null
     * @return boolean, true if paramToCheck is null or empty after trimming
     */
    fun isNullOrEmptyAfterTrim(paramToCheck: String?): Boolean {
        return null == paramToCheck || paramToCheck.trim { it <= ' ' }.isEmpty()
    }

    /**
     * Returns the string after concatenating the specified object and delimiter.
     * @param delimeter delimeter to append
     * @param objects array of String objects
     * @return
     */
    fun concatenateWithDelimeter(delimeter: String, vararg objects: String): String? {
        var concatenatedString: String? = null
        if (null != objects && objects.size > 0 && !isNullOrEmpty(delimeter)) {
            val stringBuilder = StringBuilder()
            var delimeterToAppend = false
            for (`object` in objects) {
                if (null != `object`) {
                    if (delimeterToAppend) {
                        stringBuilder.append(delimeter)
                    }
                    stringBuilder.append(`object`)
                    delimeterToAppend = true
                }
            }
            concatenatedString = stringBuilder.toString()
        }
        return concatenatedString
    }

    /**
     * To check if string is numeric
     * @param str String value to check
     * @return true or false
     */
    fun isNumeric(str: String): Boolean {
        try {
            val d = java.lang.Double.parseDouble(str)
        } catch (nfe: NumberFormatException) {
            return false
        }

        return true
    }
}
