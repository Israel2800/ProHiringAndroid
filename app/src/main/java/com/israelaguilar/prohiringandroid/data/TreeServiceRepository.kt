package com.israelaguilar.prohiringandroid.data

import com.israelaguilar.prohiringandroid.data.remote.TreeServicesApi
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import retrofit2.Call
import retrofit2.Retrofit

class TreeServiceRepository(
    private val retrofit: Retrofit
) {

    private val treeServicesApi: TreeServicesApi = retrofit.create(TreeServicesApi::class.java)


    // Mandar a llamar mediante apiary
    fun getTreeServicesApi(): Call<MutableList<TreeServiceDto>> = treeServicesApi.getTreeServicesApiary()

    fun getTreeServiceDetailApiary(id: String?): Call<TreeServiceDetailDto> = treeServicesApi.getTreeServiceDetailApiary(id)
}