package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

data class BalanceBeforeTransactionResponse(
    @SerializedName("responseCode") var responseCode : Int,
    @SerializedName("responseMessage") var responseMessage : String,
    @SerializedName("responseData") var responseData : BalanceData
)

data class BalanceData (
    @SerializedName("balance") var balance : Long
)