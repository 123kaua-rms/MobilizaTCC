package com.example.mobilizatcc.service


import com.example.mobilizatcc.model.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ClienteService {

    @POST("clientes")
    fun cadastrar(@Body cliente: Cliente): Call<Cliente>
}
