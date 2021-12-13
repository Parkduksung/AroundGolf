package base


import androidx.lifecycle.Observer
import com.example.aroundgym.base.ViewState
import org.mockito.Mock


abstract class ViewModelBaseTest : BaseTest() {

    @Mock
    lateinit var viewStateObserver: Observer<ViewState>
}