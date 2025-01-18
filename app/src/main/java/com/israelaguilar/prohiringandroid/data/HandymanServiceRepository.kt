package com.israelaguilar.prohiringandroid.data

import com.israelaguilar.prohiringandroid.data.remote.HandymanServicesApi
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.HandymanServiceDto
import retrofit2.Call
import retrofit2.Retrofit

class HandymanServiceRepository(
    private val retrofit: Retrofit
) {

    private val handymanServicesApi: HandymanServicesApi = retrofit.create(HandymanServicesApi::class.java)


    // Mandar a llamar mediante apiary
    fun getHandymanServicesApi(): Call<MutableList<HandymanServiceDto>> = handymanServicesApi.getHandymanServicesApiary()

    fun getHandymanServiceDetailApiary(id: String?): Call<HandymanServiceDetailDto> = handymanServicesApi.getHandymanServiceDetailApiary(id)
}