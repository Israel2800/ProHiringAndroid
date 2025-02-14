package com.israelaguilar.prohiringandroid.data.remote.model

import com.google.gson.annotations.SerializedName

data class PopularProjectDetailDto(
    @SerializedName("title")
    var title: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("long_desc")
    var longDesc: String? = null,

    @SerializedName("additional_detail_1")
    var additionalDetail1: String? = null,

    @SerializedName("additional_detail_2")
    var additionalDetail2: String? = null,

    @SerializedName("url_id")
    var url_id: String? = null,

    @SerializedName("latitude")
    var latitude: Double? = null,

    @SerializedName("longitude")
    var longitude: Double? = null
)
