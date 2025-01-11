package com.israelaguilar.prohiringandroid.application

import android.app.Application
import com.israelaguilar.prohiringandroid.data.PopularProjectsRepository
import com.israelaguilar.prohiringandroid.data.TreeServiceRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper

class ProHiringApp: Application() {

    // Retrofit para Tree Services
    private val retrofit by lazy {
        RetrofitHelper().getTreeServicesRetrofit()
    }

    val repository by lazy {
        TreeServiceRepository(retrofit)
    }

    // Retrofit para Popular Projects
    private val retrofitPopularProjects by lazy {
        RetrofitHelper().getPopularProjectsRetrofit()
    }

    // Repositorio para Popular Projects
    val popularProjectsRepository by lazy {
        PopularProjectsRepository(retrofitPopularProjects)
    }
}