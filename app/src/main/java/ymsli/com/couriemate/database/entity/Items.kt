package ymsli.com.couriemate.database.entity

import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("orderId")
    var orderId: String? = null,

    @SerializedName("quantity")
    var quantity: String? = null,

    @SerializedName("weight")
    var weight: String? = null,

    @SerializedName("name")
    var name: String? = null
) {
}