import com.example.mobilizatcc.model.LoginRequest
import com.example.mobilizatcc.model.RecSenhaRequest
import com.example.mobilizatcc.model.ResetSenhaRequest
import com.example.mobilizatcc.model.UsuarioRequest
import com.example.mobilizatcc.model.UsuarioResponse
import com.example.mobilizatcc.model.VerifyCodeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsuarioService {
    @Headers("Content-Type: application/json")
    @POST("usuario") // coloque o endpoint certo do seu backend
    fun registerUser(@Body usuario: UsuarioRequest): Call<UsuarioResponse>



    @POST("usuario/login") // ajuste para a rota exata do seu backend
    @Headers("Content-Type: application/json")
    fun loguinUser(@Body request: LoginRequest): Call<LoguinResponse>


    @Headers("Content-Type: application/json")
    @POST("usuario/recuperar-senha")
    fun recuperarSenha(@Body body: RecSenhaRequest): Call<Void>


    @POST("usuario/verificar-codigo")
    @Headers("Content-Type: application/json")
    fun verificarCodigo(@Body body: VerifyCodeRequest): Call<Void>


    @POST("usuario/resetar-senha")
    @Headers("Content-Type: application/json")
    fun resetarSenha(@Body body: ResetSenhaRequest): Call<Void>




}
