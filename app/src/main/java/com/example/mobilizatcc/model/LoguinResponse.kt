import com.example.mobilizatcc.model.UsuarioResponse

data class LoguinResponse(
    val status: Boolean,
    val status_code: Int,
    val token: String?,
    val usuario: UsuarioResponse? // retorna o mesmo objeto que cadastramos
)