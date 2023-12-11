package ymsli.ymsli.couriemate

import ymsli.com.couriemate.R
import ymsli.com.couriemate.model.ChangePasswordValidationModel
import ymsli.com.couriemate.utils.common.Resource
import ymsli.com.couriemate.utils.common.Validation
import ymsli.com.couriemate.utils.common.Validator
import ymsli.com.couriemate.model.UserMaster
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.contains
import java.util.regex.Pattern

class ValidationUnitTest {

    @Test
    fun validateFailedCustomerDelivery(){
        val validations = Validator.validateFailedCustomerDelivery("Others","04-11-2019", null)
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(
                    Validation.Field.RETRYDATE,
                    Resource.error(R.string.postponmentErrorMessage)
                )
            ))

        val validations2 = Validator.validateFailedCustomerDelivery("Postponment",null, null)
        assertThat(validations2,hasSize(1))
        assertThat(validations2,
            contains(
                Validation(
                    Validation.Field.RETRYDATE,
                Resource.error(R.string.postponmentErrorMessage)
            )
            ))

        val validations3 = Validator.validateFailedCustomerDelivery("Customer unreachable",null,"04-11-2019")
        assertThat(validations3,hasSize(1))
        assertThat(validations3,
            contains(
                Validation(
                    Validation.Field.FAILUREREASON,
                    Resource.error(R.string.enter_failure_comments)
                )
            ))
    }

    @Test
    fun checkValidateRefusedDelivery(){
        var validations = Validator.validateRefusedDelivery(null, "others")
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.COMMENTS,
                    Resource.error(R.string.RefusalCommentError))
            ))
        validations = Validator.validateRefusedDelivery("Customer denied", "others")
        assertThat(validations,hasSize(0))

        validations = Validator.validateRefusedDelivery("    ", "others")

        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.COMMENTS,
                    Resource.error(R.string.RefusalCommentError))
            ))
    }


    @Test
    fun checkTransactionRegistration_nullAmount(){
        val validations = Validator.validateTransactionRegistrationInput(null,null,true,true)
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.AMOUNT_PAID,
                    Resource.error(R.string.amount_error))
            ))
    }

    @Test
    fun checkTransactionRegistration_nullSignature(){
        val validations = Validator.validateTransactionRegistrationInput("12",null,false,false)
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.SIGNATURE,
                    Resource.error(R.string.signature_validation))
            ))
    }

    @Test
    fun checkTransactionRegistration_nullSignature_bankDeposit(){
        val validations = Validator.validateTransactionRegistrationInput("12","signature".toByteArray(),false,true)
        assertThat(validations,hasSize(0))
    }

    @Test
    fun checkTransactionRegistration_amount_decimal(){
        val validations = Validator.validateTransactionRegistrationInput("12.00","signature".toByteArray(),false,true)
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.AMOUNT_PAID,
                    Resource.error(R.string.invalid_amount_error))
            ))
    }

    @Test
    fun checkTransactionRegistration_amount_string(){
        val validations = Validator.validateTransactionRegistrationInput("jkbwdf","signature".toByteArray(),false,true)
        assertThat(validations,hasSize(1))
        assertThat(validations,
            contains(
                Validation(Validation.Field.AMOUNT_PAID,
                    Resource.error(R.string.invalid_amount_error))
            ))
    }

    @Test
    fun checkTransactionRegistration_amount_number_valid(){
        val validations = Validator.validateTransactionRegistrationInput("1231","signature".toByteArray(),false,true)
        assertThat(validations,hasSize(0))
    }
}