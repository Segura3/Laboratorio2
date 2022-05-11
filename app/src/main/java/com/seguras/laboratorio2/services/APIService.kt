package com.seguras.laboratorio2.services

import com.seguras.laboratorio2.model.Login
import com.seguras.laboratorio2.model.Restaurante
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getRestaurantes(@Url url: String): Response<ArrayList<Restaurante>>
    @GET
    suspend fun login(@Url url: String): Response<Login>
}