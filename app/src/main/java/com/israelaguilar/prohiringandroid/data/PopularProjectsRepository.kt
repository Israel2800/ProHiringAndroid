package com.israelaguilar.prohiringandroid.data

import com.israelaguilar.prohiringandroid.data.remote.PopularProjectsApi
import com.israelaguilar.prohiringandroid.data.remote.TreeServicesApi
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProject
import com.israelaguilar.prohiringandroid.data.remote.model.PopularProjectDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import retrofit2.Call
import retrofit2.Retrofit

class PopularProjectsRepository(
    private val retrofit: Retrofit
) {

    private val popularProjectsApi: PopularProjectsApi = retrofit.create(PopularProjectsApi::class.java)


    // Mandar a llamar mediante apiary
    fun getPopularProjectsApi(): Call<MutableList<PopularProject>> = popularProjectsApi.getPopularProjectsApiary()

    fun getPopularProjectDetailApiary(id: String?): Call<PopularProjectDetailDto> = popularProjectsApi.getPopularProjectDetailApiary(id)
}