package com.example.aroundgym.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import com.example.aroundgym.R
import com.example.aroundgym.base.BaseActivity
import com.example.aroundgym.base.ViewState
import com.example.aroundgym.databinding.ActivityMainBinding
import com.example.aroundgym.ext.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()
        initViewModel()
    }


    private fun initUi() {
        supportFragmentManager.beginTransaction().add(
            R.id.container_main,
            SearchFragment.newInstance(),
            SearchFragment.TAG
        ).commit()
    }

    private fun initViewModel() {
        mainViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? MainViewModel.MainViewState)?.let {
                onChangedMainViewState(
                    viewState
                )
            }
        }
    }

    private fun onChangedMainViewState(viewState: MainViewModel.MainViewState) {
        when (viewState) {
            is MainViewModel.MainViewState.RouteSearch -> {
                supportFragmentManager.popBackStack(
                    DetailFragment.TAG,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }

            is MainViewModel.MainViewState.RouteDetail -> {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container_main,
                        DetailFragment.newInstance(viewState.item),
                        DetailFragment.TAG
                    ).addToBackStack(DetailFragment.TAG).commit()
                hideKeyboard()
            }
        }
    }
}