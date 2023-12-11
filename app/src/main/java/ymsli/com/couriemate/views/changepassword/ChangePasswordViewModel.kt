package ymsli.com.couriemate.views.changepassword

import androidx.lifecycle.MutableLiveData
import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseViewModel
import ymsli.com.couriemate.model.ChangePasswordRequestDTO
import ymsli.com.couriemate.model.ChangePasswordValidationModel
import ymsli.com.couriemate.repository.CouriemateRepository
import ymsli.com.couriemate.utils.NetworkHelper
import ymsli.com.couriemate.utils.common.*
import ymsli.com.couriemate.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj Singh (VE00YM023)
 * @date   Oct 15, 2019
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -------------------------------------------------------------------------------------
 * ChangePasswordViewModel : View model for the ChangePasswordFragment
 * -------------------------------------------------------------------------------------
 * Revision History
 * -------------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -------------------------------------------------------------------------------------
 */
class ChangePasswordViewModel(schedulerProvider: SchedulerProvider,
                              compositeDisposable: CompositeDisposable,
                              networkHelper: NetworkHelper,
                              private val couriemateRepository: CouriemateRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,couriemateRepository) {

    /** Live data fields for all the text input fields on the UI */
    var currentPassword = couriemateRepository.getUserPassword()
    var enteredCurrentPassword: MutableLiveData<String> = MutableLiveData()
    var newPassword: MutableLiveData<String> = MutableLiveData()
    var retypeNewPassword: MutableLiveData<String> = MutableLiveData()
    var apiRequestActive: MutableLiveData<Boolean> = MutableLiveData()

    private val validationsList: MutableLiveData<List<Validation>> = MutableLiveData()

    fun onCurrentPasswordChange(fieldValue: String) = enteredCurrentPassword.postValue(fieldValue)
    fun onNewPasswordChange(fieldValue: String) = newPassword.postValue(fieldValue)
    fun onRetypeNewPasswordChange(fieldValue: String) = retypeNewPassword.postValue(fieldValue)

    override fun onCreate() {}

    /**
     * Used to perform the change password operation.
     * If all validations are passed and network is available then API call is performed.
     * @author Balraj VE00YM023
     */
    fun changePassword(){
        val validationModel = getValidationModel()
        val validations = Validator.validateChangePasswordFields(validationModel)
        validationsList.postValue(validations)
        val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

        when{
            /** Validation failed, notify user */
            successValidation.size != validations.size -> {
                val failedValidation = validations.filter { it.resource.status == Status.ERROR }[0]
                messageStringId.postValue(failedValidation.resource)
            }

            /** Network is not available, notify user */
            !checkInternetConnection() -> {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
            }

            /** Network is available and validations passed, continue with API call */
            else -> {
                apiRequestActive.postValue(true)
                compositeDisposable.addAll(
                    couriemateRepository.changePassword(getRequestModel())
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            apiRequestActive.postValue(false)
                            couriemateRepository.updateUserPassword(validationModel.newPassword!!)
                            currentPassword = validationModel.newPassword
                            messageStringId.postValue(Resource.success(R.string.PASSWORD_CHANGE_SUCCESS))
                        }, {
                            apiRequestActive.postValue(false)
                            messageStringId.postValue(Resource.error(R.string.ERROR_PASSWORD_CHANGE_FAILED))
                        })
                )
            }
        }
    }

    /**
     * Prepare a model for the validator.
     * set all the fields which are required by the validator.
     * @author Balraj VE00YM023
     */
    private fun getValidationModel(): ChangePasswordValidationModel {
        return ChangePasswordValidationModel(
            currentPassword = currentPassword!!,
            enteredCurrentPassword = enteredCurrentPassword.value,
            newPassword = newPassword.value,
            retypeNewPassword = retypeNewPassword.value
        )
    }

    /**
     * Prepare a model for the request API.
     * @author Balraj VE00YM023
     */
    private fun getRequestModel(): ChangePasswordRequestDTO {
        return ChangePasswordRequestDTO(
            currentPassword = currentPassword,
            newPassword = newPassword.value, userId = couriemateRepository.getUserName(),
            timezoneOffset = Utils.getGMTOffset()
        )
    }
}