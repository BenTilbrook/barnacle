package com.github.bentilbrook.barnacle.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.bentilbrook.barnacle.EpicMiddleware
import com.github.bentilbrook.barnacle.Store
import com.github.bentilbrook.barnacle.composeMiddleware
import com.github.bentilbrook.barnacle.sample.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var store: Store<AppState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        store = Store(
            initialState = lastNonConfigurationInstance as AppState?
                ?: savedInstanceState?.getParcelable(KEY_APP_STATE)
                ?: AppState(),
            reducer = ::rootReducer,
            middleware = composeMiddleware(EpicMiddleware(::rootEpic))
        )

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

private const val KEY_APP_STATE = "appState"
