package com.example.aroundgym.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aroundgym.R
import com.example.aroundgym.base.BaseFragment
import com.example.aroundgym.base.ViewState
import com.example.aroundgym.databinding.FragmentSearchBinding
import com.example.aroundgym.ext.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val bookAdapter by lazy { BookAdapter() }

    private var isLoad = false

    private val mainViewModel by activityViewModels<MainViewModel>()

    private val searchViewModel by viewModels<SearchViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {

        binding.rvBook.run {
            adapter = bookAdapter
        }

        bookAdapter.setOnItemClickListener { item ->
            mainViewModel.routeDetail(item)
        }

        binding.rvBook.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                searchViewModel.onScrolled(
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition(),
                    bookAdapter.itemCount
                )
            }
        })
    }

    private fun initViewModel() {

        binding.viewModel = searchViewModel

        mainViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? MainViewModel.MainViewState)?.let {
                onChangedMainViewState(
                    viewState
                )
            }
        }
        searchViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? SearchViewModel.SearchViewState)?.let {
                onChangedSearchViewState(
                    viewState
                )
            }
        }
    }

    private fun onChangedMainViewState(viewState: MainViewModel.MainViewState) {
        when (viewState) {
            is MainViewModel.MainViewState.ToggleLike -> {
                bookAdapter.toggleLike(viewState.item)
            }
        }
    }

    private fun onChangedSearchViewState(viewState: SearchViewModel.SearchViewState) {
        when (viewState) {
            is SearchViewModel.SearchViewState.GetSearchBookList -> {
                bookAdapter.clear()
                bookAdapter.addAll(viewState.list)
                isLoad = true
            }

            is SearchViewModel.SearchViewState.GetLoadNextPageList -> {
                bookAdapter.loadNextData(viewState.list)
                isLoad = true
            }
            is SearchViewModel.SearchViewState.PageEnd -> {
                showToast(message = getString(R.string.message_page_end))
            }

            is SearchViewModel.SearchViewState.Error -> {
                showToast(message = viewState.message)
            }

            is SearchViewModel.SearchViewState.EmptyInput -> {
                showToast(message = getString(R.string.message_empty_input))
            }
        }
    }

    companion object {

        const val TAG = "SearchFragment"

        fun newInstance() =
            SearchFragment()

    }
}
