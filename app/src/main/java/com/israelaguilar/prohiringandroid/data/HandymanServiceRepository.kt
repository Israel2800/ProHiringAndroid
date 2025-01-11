package com.israelaguilar.prohiringandroid.data

import com.israelaguilar.prohiringandroid.data.remote.HandymanServicesApi
import com.israelaguilar.prohiringandroid.data.remote.TreeServicesApi
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServicesDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import retrofit2.Call
import retrofit2.Retrofit

class HandymanServiceRepository(
    private val retrofit: Retrofit
) {

    private val handymanServicesApi: HandymanServicesApi = retrofit.create(HandymanServicesApi::class.java)


    // Mandar a llamar mediante apiary
    fun getHandymanServicesApi(): Call<MutableList<HandymanServicesDto>> = handymanServicesApi.getHandymanServicesApiary()

    //fun getTreeServiceDetailApiary(id: String?): Call<TreeServiceDetailDto> = treeServicesApi.getTreeServiceDetailApiary(id)
}