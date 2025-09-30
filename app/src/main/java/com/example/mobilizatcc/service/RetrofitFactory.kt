    package com.example.mobilizatcc.service

    import UsuarioService
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    class RetrofitFactory {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://10.107.134.9:8080/") // seu backend
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getUsuarioService(): UsuarioService {
            return retrofit.create(UsuarioService::class.java)
        }

        fun getAuthService(): AuthService {
            return retrofit.create(AuthService::class.java)
        }
    }
