/**
 * Project Name : YMLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   JULY 12, 2018
 * Description

 * -----------------------------------------------------------------------------------

 * LocaleUtils Class :  This class provide methods to change locale an change resources
 * with layout direction

 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description

 * -----------------------------------------------------------------------------------

 */

package `in`.ymsli.ymlibrary.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*
import android.util.DisplayMetrics



class LocaleUtils(base: Context) : ContextWrapper(base) {


    companion object {

        // Singleton Object to provide LocaleUtils object
        private var mInstance: LocaleUtils? = null
        @Synchronized
        fun getInstance(base: Context): LocaleUtils {
            if (mInstance == null) {
                mInstance = LocaleUtils(base);
            }
            return mInstance as LocaleUtils
        }
    }


    /**
         * Method sets locale at run time
         * @param context instance of Context
         * @param language to change local
         */
        fun setLocale(context: Context, language: String): Context {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else updateResourcesLegacy(context, language)

        }

        /**
         * Method to update resources and layout direction
         * @param context instance of Context
         * @param language to change local
         */
        private fun updateResources(context: Context, language: String): Context {
            val res = context.resources
            val dm = res.displayMetrics
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            res.updateConfiguration(configuration,dm)
            return context.createConfigurationContext(configuration)
        }

        /**
         * Method to update resources and layout direction
         * @param context instance of Context
         * @param language to change local
         */
        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val res = context.resources
            var locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = resources.configuration
            val dm = res.displayMetrics
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            res.updateConfiguration(configuration,dm)
            val context = createConfigurationContext(configuration)

            return context
        }
    }