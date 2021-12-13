package com.example.aroundgolf.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.aroundgolf.R
import com.example.aroundgolf.base.BaseFragment
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.databinding.BookmarkFragmentBinding
import com.example.aroundgolf.viewmodel.BookmarkViewModel
import com.example.aroundgolf.viewmodel.HomeViewModel

class BookmarkFragment : BaseFragment<BookmarkFragmentBinding>(R.layout.bookmark_fragment) {


    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initUi() {
//        startBookmarkAdapter()
    }

    private fun initViewModel() {
        lifecycle.addObserver(bookmarkViewModel)

        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let {
                onChangedHomeViewState(viewState)
            }

        }

        bookmarkViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? BookmarkViewModel.BookmarkViewState)?.let {
                onChangedBookmarkViewState(viewState)
            }
        }
    }


    private fun onChangedBookmarkViewState(viewState: BookmarkViewModel.BookmarkViewState) {
        when (viewState) {
//            is BookmarkViewModel.BookmarkViewState.BookmarkList -> {
//                bookmarkAdapter.addAllBookmarkData(viewState.bookmarkList)
//                binding.bookmarkRv.isVisible = true
//                binding.tvEmptyBookmark.isVisible = false
//            }
//            is BookmarkViewModel.BookmarkViewState.Error -> {
//                showToast(message = viewState.errorMessage)
//            }
//
//            is BookmarkViewModel.BookmarkViewState.EmptyBookmarkList -> {
//                binding.bookmarkRv.isVisible = false
//                binding.tvEmptyBookmark.isVisible = true
//            }
//
//            is BookmarkViewModel.BookmarkViewState.ShowLoginView -> {
//                homeViewModel.startLoginView()
//            }
        }
    }


    private fun onChangedHomeViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {
//            is HomeViewModel.HomeViewState.AddBookmarkItem -> {
//                bookmarkAdapter.addBookmark(viewState.item)
//                binding.bookmarkRv.isVisible = true
//                binding.tvEmptyBookmark.isVisible = false
//            }
//            is HomeViewModel.HomeViewState.DeleteBookmarkItem -> {
//                bookmarkAdapter.deleteBookmark(viewState.item)
//                if (bookmarkAdapter.itemCount == 0) {
//                    binding.bookmarkRv.isVisible = false
//                    binding.tvEmptyBookmark.isVisible = true
//                }
//            }
        }
    }


//    private fun startBookmarkAdapter() {
//        binding.bookmarkRv.run {
//            adapter = bookmarkAdapter
//            bookmarkAdapter.clear()
//            layoutManager = LinearLayoutManager(requireContext())
//            bookmarkAdapter.setBookmarkItemClickListener(this@BookmarkFragment)
//        }
//    }

}