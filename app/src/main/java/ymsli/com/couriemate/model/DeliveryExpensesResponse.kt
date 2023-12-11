package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

data class DeliveryExpensesResponse (
    @SerializedName("responseCode") var responseCode : Int,
    @SerializedName("responseMessage") var responseMessage : String,
    @SerializedName("responseData") var responseData : List<DeliveryExpenses>
        )

data class DeliveryExpenses (

    @SerializedName("expenditureId") var expenditureId : Long,
    @SerializedName("transactionNo") var transactionNo : Long,
    @SerializedName("transactionDate") var transactionDate : String,
    @SerializedName("formattedTransactionDate") var formattedTransactionDate : String,
    @SerializedName("className") var className : String,
    @SerializedName("itemId") var itemId : Int,
    @SerializedName("personId") var personId : Int,
    @SerializedName("description") var description : String,
    @SerializedName("isReciept") var isReciept : Boolean,
    @SerializedName("oldIsReciept") var oldIsReciept : Boolean,
    @SerializedName("amount") var amount : Long,
    @SerializedName("balance") var balance : Long,
    @SerializedName("receieverSignature") var receieverSignature : String,
    @SerializedName("receieverSignatureString") var receieverSignatureString : String,
    @SerializedName("visibleindex") var visibleindex : String,
    @SerializedName("rowstate") var rowstate : String

)