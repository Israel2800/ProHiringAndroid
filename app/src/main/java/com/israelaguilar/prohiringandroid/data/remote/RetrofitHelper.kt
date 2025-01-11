/*
package com.israelaguilar.prohiringandroid.data.remote

import com.israelaguilar.prohiringandroid.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    fun getRetrofit(): Retrofit {

        val apiKey = "AIzaSyDW3zbpWFqDjDQruTpvTQYI0r_zcJpqg6M"

        val interceptor = HttpLoggingInterceptor().apply {
            //Para que el interceptor me dé mensajes a nivel del
            //body de la operación de la red
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(ApiKeyInterceptor(apiKey))
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
*/

package com.israelaguilar.prohiringandroid.data.remote

import com.israelaguilar.prohiringandroid.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    private val apiKey = "AIzaSyDW3zbpWFqDjDQruTpvTQYI0r_zcJpqg6M"

    // Interceptor para el logging
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente de OkHttp con el interceptor y el ApiKeyInterceptor
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        addInterceptor(ApiKeyInterceptor(apiKey))
    }.build()

    // Método para obtener el Retrofit para la primera API (TreeServicesApi)
    fun getTreeServicesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPopularProjectsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_POPULAR_PROJECTS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Método para obtener el Retrofit para la nueva API HandymanServices
    fun getHandymanServicesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_HANDYMAN_SERVICES)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}


