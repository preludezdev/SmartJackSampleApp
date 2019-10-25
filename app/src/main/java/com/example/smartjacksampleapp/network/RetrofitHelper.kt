package com.example.smartjacksampleapp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper private constructor() {
    private val gson: Gson by lazy { GsonBuilder().setLenient().create() }

    private val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    companion object {
        const val API_URL = "https://eq0lwb7e8e.execute-api.ap-northeast-2.amazonaws.com/"
        private var INSTANCE: RetrofitHelper? = null

        fun getInstance() = INSTANCE ?: RetrofitHelper().apply { INSTANCE = this }
    }
}