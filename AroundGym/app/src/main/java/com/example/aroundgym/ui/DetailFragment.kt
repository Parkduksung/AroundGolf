package com.example.aroundgym.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.aroundgym.BR
import com.example.aroundgym.R
import com.example.aroundgym.base.BaseFragment
import com.example.aroundgym.base.ViewState
import com.example.aroundgym.data.model.BookItem
import com.example.aroundgym.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModel()

    }


    private fun initUi() {

        binding.run {
            setVariable(
                BR.item,
                Pair(arguments?.getParcelable<BookItem>(KEY_ITEM), arguments?.getBoolean(KEY_LIKE))
            )

            cbBookmark.setOnCheckedChangeListener { view, isChecked ->
                view.background = if (isChecked) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_like_on)
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_like_off)
                }
                mainViewModel.toggleLike(item!!.copy(second = isChecked))
            }
        }
    }

    private fun initViewModel() {

        binding.viewModel = detailViewModel

        detailViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? DetailViewModel.DetailViewState)?.let {
                onChangedDetailViewState(
                    viewState
                )
            }
        }
    }

    private fun onChangedDetailViewState(viewState: DetailViewModel.DetailViewState) {
        when (viewState) {
            is DetailViewModel.DetailViewState.RouteSearch -> {
                mainViewModel.routSearch()
            }
        }
    }

    companion object {

        const val TAG = "DetailFragment"

        private const val KEY_ITEM = "key_item"
        private const val KEY_LIKE = "key_like"

        fun newInstance(
            item: Pair<BookItem, Boolean>
        ) = DetailFragment().apply {
            arguments =
                Bundle().apply {
                    putParcelable(KEY_ITEM, item.first)
                    putBoolean(KEY_LIKE, item.second)
                }
        }
    }
}