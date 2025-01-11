package com.israelaguilar.prohiringandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class HandymanServicesDto (

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("thumbnail")
    var thumbnail: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("descrip")
    var descrip: String? = null,

    @SerializedName("price")
    var price: String? = null,

    @SerializedName("duration")
    var duration: String? = null

)