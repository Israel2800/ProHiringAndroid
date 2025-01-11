package com.israelaguilar.prohiringandroid.data.remote

import com.israelaguilar.prohiringandroid.data.remote.model.PopularProject
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDetailDto
import com.israelaguilar.prohiringandroid.data.remote.model.TreeServiceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PopularProjectsApi {

    // Apiary

    // https://private-c0eaf-treeservices1.apiary-mock.com/treeServices/treeServices_list
    @GET("popularProjects/popularProjects_list")
    fun getPopularProjectsApiary(): Call<MutableList<PopularProject>>

    // https://private-c0eaf-treeservices1.apiary-mock.com/treeServices/treeService_detail/21357
    /*@GET("treeServices/treeService_detail/{id}")
    fun getTreeServiceDetailApiary(
        @Path("id") id: String?
    ): Call<TreeServiceDetailDto>*/
}