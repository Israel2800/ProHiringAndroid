package com.israelaguilar.prohiringandroid.application

import android.app.Application
import com.israelaguilar.prohiringandroid.data.TreeServiceRepository
import com.israelaguilar.prohiringandroid.data.remote.RetrofitHelper

class VideoGamesRFApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        TreeServiceRepository(retrofit)
    }

}