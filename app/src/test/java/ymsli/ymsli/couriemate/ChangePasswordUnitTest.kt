package ymsli.ymsli.couriemate

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.couriemate.R
import ymsli.com.couriemate.model.ChangePasswordValidationModel
import ymsli.com.couriemate.utils.common.Resource
import ymsli.com.couriemate.utils.common.Validation
import ymsli.com.couriemate.utils.common.Validator
import java.text.SimpleDateFormat

class ChangePasswordUnitTest {
    @Test
    fun validateNewPasswordLength() {
        var changePasswordValidationModel = ChangePasswordValidationModel("abcd", "abcd", "123", "123")
        var validations = Validator.validateChangePasswordFields(changePasswordValidationModel)
        MatcherAssert.assertThat(validations, Matchers.hasSize(1))
        MatcherAssert.assertThat(validations,
                Matchers.contains(
                        Validation(
                                Validation.Field.NEW_PASSWORD,
                                Resource.error(R.string.ERROR_PASSWORD_LENGTH)
                        )
                ))
    }

    @Test
    fun validateEmptyNewPasswordLength() {

        var changePasswordValidationModel = ChangePasswordValidationModel("ABC123", "ABC123"
                , "123ABC", "")
        var  validations = Validator.validateChangePasswordFields(changePasswordValidationModel)
        MatcherAssert.assertThat(validations, Matchers.hasSize(1))
        MatcherAssert.assertThat(validations,
                Matchers.contains(
                        Validation(
                                Validation.Field.RETYPE_NEW_PASSWORD,
                                Resource.error(R.string.ERROR_EMPTY_RETYPE_NEW_PASSWORD)
                        )
                ))
    }
    @Test
    fun validateMismatchNewPassword() {
        var changePasswordValidationModel = ChangePasswordValidationModel("ABC123", "ABC123"
                , "123ABC", "XYZPQR123")
        var validations = Validator.validateChangePasswordFields(changePasswordValidationModel)
        MatcherAssert.assertThat(validations, Matchers.hasSize(1))
        MatcherAssert.assertThat(validations,
                Matchers.contains(
                        Validation(
                                Validation.Field.RETYPE_NEW_PASSWORD,
                                Resource.error(R.string.ERROR_MISMATCH_RETYPE_NEW_PASSWORD)
                        )
                ))
    }
    @Test
    fun validateMismatchCurrentPassword() {
        var changePasswordValidationModel = ChangePasswordValidationModel("ABC123PQR","ABC123"
                ,"123ABC","123ABC")
        var validations = Validator.validateChangePasswordFields(changePasswordValidationModel)
        MatcherAssert.assertThat(validations, Matchers.hasSize(1))
        MatcherAssert.assertThat(validations,
                Matchers.contains(
                        Validation(
                                Validation.Field.CURRENT_PASSWORD,
                                Resource.error(R.string.ERROR_MISMATCH_CURRENT_PASSWORD)
                        )
                ))
    }

}