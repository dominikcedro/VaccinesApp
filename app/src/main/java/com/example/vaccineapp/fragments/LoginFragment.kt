import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.vaccineapp.R
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.json.JsonPlugin
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

class LoginFragment : Fragment() {

//    private val httpClient = HttpClient(Android) {
//        install(JsonPlugin) {
//            serializer = KotlinxSerializer(Json {
//                ignoreUnknownKeys = true
//            })
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        val usernameEditText = view.findViewById<EditText>(R.id.etLoginPassword)
        val passwordEditText = view.findViewById<EditText>(R.id.etLoginPassword)


        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

        }

        return view
    }
}