package com.example.smartjacksampleapp.network.vo

import com.google.gson.annotations.SerializedName

data class SmartJackRequest(
    @SerializedName("value")
    val value: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String
)