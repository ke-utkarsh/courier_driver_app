
package ymsli.com.couriemate.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewModelProviderFactory : Used for the creation of instance of ViewModel of
 *                              Activity. Since the constructor of view model can
 *                              be controlled directly by dagger2 framework, a factory
 *                              provider class is required
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class ViewModelProviderFactory <T : ViewModel>(private val kClass: KClass<T>,
                                               private val creator: () -> T):ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(kClass.java)) return creator() as T
        throw IllegalArgumentException("Unknown class name")
    }
}