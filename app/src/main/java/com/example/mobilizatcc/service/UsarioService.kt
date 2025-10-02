import com.example.mobilizatcc.model.LoginRequest
import com.example.mobilizatcc.model.UsuarioRequest
import com.example.mobilizatcc.model.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsuarioService {
    @Headers("Content-Type: application/json")
    @POST("usuario") // coloque o endpoint certo do seu backend
    fun registerUser(@Body usuario: UsuarioRequest): Call<UsuarioResponse>
}
