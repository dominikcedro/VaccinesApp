import HttpService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.UserDetails
import com.example.vaccineapp.service.TokenManager
import kotlinx.coroutines.launch

class ListOfUsersViewModel(private val tokenManager: TokenManager, private val httpService: HttpService) : ViewModel() {
    private val _usersList = MutableLiveData<List<UserDetails>>()
    val usersList: LiveData<List<UserDetails>> get() = _usersList

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val usersList = httpService.getAllUsers(tokenManager.getJwtToken() ?: "")
                _usersList.postValue(usersList)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}