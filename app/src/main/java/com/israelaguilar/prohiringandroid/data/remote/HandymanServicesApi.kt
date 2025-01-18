package com.israelaguilar.prohiringandroid.data.remote

import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HandymanServicesApi {

    // Apiary

    // https://private-c0eaf-treeservices1.apiary-mock.com/treeServices/treeServices_list
    @GET("handymanServices/service_list")
    fun getHandymanServicesApiary(): Call<MutableList<HandymanServiceDto>>

    // https://private-c0eaf-treeservices1.apiary-mock.com/treeServices/treeService_detail/21357
    @GET("handymanServices/service_detail/{id}")
    fun getHandymanServiceDetailApiary(
        @Path("id") id: String?
    ): Call<HandymanServiceDetailDto>

}