package ymsli.com.couriemate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   Dec 10, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * LocationTrackingRequest : Request model for insertLocation API.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
@Entity
class LocationTrackingRequest(

    @SerializedName("tripId")
     val tripId: String,

    @SerializedName("companyCode")
     val companyCode: String,

    @SerializedName("thirdPartySysRiderId")
     val thirdPartySysRiderId: Long,

    @SerializedName("lat")
     val lat: Double,

    @SerializedName("long_")
     val long_: Double,

    @SerializedName("speed")
     val speed: Double,

    @PrimaryKey
    @SerializedName("deviceTimestamp")
     val deviceTimestamp: String


    )

