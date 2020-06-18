package com.github.bentilbrook.barnacle.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.bentilbrook.barnacle.EpicMiddleware
import com.github.bentilbrook.barnacle.Store
import com.github.bentilbrook.barnacle.composeMiddleware
import com.github.bentilbrook.barnacle.sample.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //    private val viewModel by viewModels<MainViewModel>()
    private lateinit var store: Store<AppState>

    @Inject lateinit var appEpic: AppEpic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        store = Store(
            initialState = lastCustomNonConfigurationInstance as AppState?
                ?: savedInstanceState?.getParcelable(KEY_APP_STATE)
                ?: AppState(),
            reducer = ::appReducer,
            middleware = composeMiddleware(EpicMiddleware(appEpic))
        )
//        val store = viewModel.store

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppComponent(
            state = store.state(),
            dispatch = store::dispatch,
            parent = binding.root,
            scope = lifecycleScope
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_APP_STATE, store.state().value)
    }

    override fun onRetainCustomNonConfigurationInstance() = store.state().value
}

//class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
//    val store = Store(
//        initialState = savedStateHandle.get(KEY_APP_STATE) ?: AppState(),
//        reducer = ::rootReducer,
//        middleware = composeMiddleware(EpicMiddleware(::rootEpic))
//    )
//}

private const val KEY_APP_STATE = "appState"
