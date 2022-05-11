package com.seguras.laboratorio2.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Login(
    @SerializedName("status")
    var status: String,
    @SerializedName("userName")
    var userName: String,
    @SerializedName("token")
    var token: String
): Serializable
