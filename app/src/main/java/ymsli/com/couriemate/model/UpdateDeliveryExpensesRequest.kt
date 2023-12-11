package ymsli.com.couriemate.model

import com.google.gson.annotations.SerializedName

data class UpdateDeliveryExpensesRequest (
    @SerializedName("detailList") var detailList : List<DetailList>,
    @SerializedName("source") var source : String,
    @SerializedName("userName") var userName : String
    )

data class DetailList (

    @SerializedName("amount") var amount : Int,
    @SerializedName("expenditureId") var expenditureId : Long?,
    @SerializedName("description") var description : String?,
    @SerializedName("isReciept") var isReciept : Boolean,
    @SerializedName("itemId") var itemId : Int,
    @SerializedName("personId") var personId : Int,
    @SerializedName("receieverSignature") var receieverSignature : ByteArray?,
    @SerializedName("rowstate") var rowstate : String

)