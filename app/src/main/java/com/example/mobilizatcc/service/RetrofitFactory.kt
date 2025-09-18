package com.example.mobilizatcc.service


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/") // muda para seu servidor/porta
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClienteService(): ClienteService {
        return retrofit.create(ClienteService::class.java)
    }
}
