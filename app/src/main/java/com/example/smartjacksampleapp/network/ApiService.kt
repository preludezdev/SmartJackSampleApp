package com.example.smartjacksampleapp.network

import com.example.smartjacksampleapp.network.vo.SmartJackRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("default/android-dev-recruit/")
    fun postData(@Body param: SmartJackRequest): Call<String>
}