/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   August 2, 2018

 * Description
 * -----------------------------------------------------------------------------------

 * PhoneNumberValidator Class : Responsible for validating phone numbers company wise

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.utils.validator

import `in`.ymsli.ymlibrary.config.YMConstants
import `in`.ymsli.ymlibrary.errors.YMErrorCode
import `in`.ymsli.ymlibrary.errors.YMExceptions
import `in`.ymsli.ymlibrary.utils.CommonUtils
import android.util.Log

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

class PhoneNumberValidator {


    /**
     * Convert String number to PhoneNumber object
     * @param phoneNumberStr number
     * @param countryCode countryCode
     */
    @Throws(YMExceptions::class)
    private fun formatStringtoPhoneNumber(phoneNumberStr: String, countryCode: String): Phonenumber.PhoneNumber? {

        val phoneUtil = PhoneNumberUtil.getInstance()
        var phoneNumberProto: Phonenumber.PhoneNumber? = null

        try {
            phoneNumberProto = phoneUtil.parse(phoneNumberStr, countryCode)
        } catch (e: NumberParseException) {
            e.printStackTrace()
            throw YMExceptions(YMErrorCode.PHONE_NUMBER_OR_COUNTRY_CODE_NOT_VALID, YMConstants.EMPTY_STRING)
        }

        return phoneNumberProto
    }


    /**
     * Provide result true if phone number is valid
     * @param number mobile number
     * @param countryCode country code to validate with
     */
    fun isPhoneNumberValidForRegion(number: String, countryCode: String): Boolean {

        try {
            if (PhoneNumberUtil.getInstance().isValidNumberForRegion(formatStringtoPhoneNumber(number, countryCode)!!, countryCode))
                return true
        } catch (ymExceptions: YMExceptions) {
            ymExceptions.printStackTrace()
            CommonUtils.showLog(Log.ERROR, ymExceptions.errorMessage)
        }

        return false


    }

    companion object {

        private var instance: PhoneNumberValidator? = null
        @Synchronized
        fun getInstance(): PhoneNumberValidator {
            if (instance == null) {
                instance = PhoneNumberValidator()
            }

            return instance as PhoneNumberValidator
        }
    }
}


