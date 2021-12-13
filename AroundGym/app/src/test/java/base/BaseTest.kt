package base

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
abstract class BaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var application: Application

    @ExperimentalCoroutinesApi
    @Before
    open fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }
}
