import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccineapp.domain.NewsArticle
import kotlinx.coroutines.launch

class NewsArticleViewModel(private val httpService: HttpService) : ViewModel() {
    private val _article = MutableLiveData<NewsArticle>()
    val article: LiveData<NewsArticle> get() = _article

    fun fetchArticle() {
        viewModelScope.launch {
            val newsArticle = httpService.fetchNewsArticle()
            _article.value = newsArticle
        }
    }
}