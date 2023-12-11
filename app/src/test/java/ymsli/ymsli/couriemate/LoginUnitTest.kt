package ymsli.ymsli.couriemate

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import ymsli.com.couriemate.R
import ymsli.com.couriemate.model.UserMaster
import ymsli.com.couriemate.utils.common.Resource
import ymsli.com.couriemate.utils.common.Validation
import ymsli.com.couriemate.utils.common.Validator

class LoginUnitTest {
    @Test
    fun validateBothCorrectUserCredentials() {
        var user =
                UserMaster(username = "abcded", password = "password")
        val validations1 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations1, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations1,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.success()
                        ),
                        Validation(
                                Validation.Field.PASSWORD,
                                Resource.success()
                        )
                ))

        // in case

    }
    @Test
    fun validateBothCorrectUserCredentialsSpecialChar() {
        var user = UserMaster(
                username = "abcd234ed",
                password = "passw!@#34 ord"
        )
        val validations2 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations2, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations2,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.success()
                        ),
                        Validation(
                                Validation.Field.PASSWORD,
                                Resource.success()
                        )
                ))
    }

    @Test
    fun validateBothCorrectUserCredentialsUsernameInt() {
        var user = UserMaster(username = "1234", password = "password")
        val validations3 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations3, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations3,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.success()
                        ),
                        Validation(
                                Validation.Field.PASSWORD,
                                Resource.success()
                        )
                ))

    }

    @Test
    fun validateIncorrectPasswordLength() {
        var user = UserMaster(username = "023948###", password = "pass")
        val validations4 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations4, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations4,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.success()
                        ),
                        Validation(
                                Validation.Field.PASSWORD,
                                Resource.error(R.string.password_field_small_length)
                        )
                ))

    }
    @Test
    fun validateEmptyPasswordField() {
        var user = UserMaster(username = "abcded", password = "")
        val validations5 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations5, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations5,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.success()
                        ),
                        Validation(
                                Validation.Field.EMPTY_PASSWORD,
                                Resource.error(R.string.password_field_empty)
                        )
                ))

    }
    @Test
    fun validateBothEmptyField() {
        var user = UserMaster(username = "", password = "")
        val validations6 = Validator.validateLoginFields(user)
        MatcherAssert.assertThat(validations6, Matchers.hasSize(2))
        MatcherAssert.assertThat(validations6,
                Matchers.contains(
                        Validation(
                                Validation.Field.USERNAME,
                                Resource.error(R.string.username_field_empty)
                        ),
                        Validation(
                                Validation.Field.EMPTY_PASSWORD,
                                Resource.error(R.string.password_field_empty)
                        )
                ))
    }


}