package com.example.aroundgolf.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aroundgolf.BuildConfig
import com.example.aroundgolf.R
import com.example.aroundgolf.base.BaseActivity
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.databinding.ActivityHomeBinding
import com.example.aroundgolf.ext.showToast
import com.example.aroundgolf.ui.bookmark.BookmarkFragment
import com.example.aroundgolf.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val homeViewModel by viewModel<HomeViewModel>()

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.icon = resources.obtainTypedArray(R.array.array_tab_icon).getDrawable(position)
        }

    private var backWait: Long = INIT_TIME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycle.addObserver(homeViewModel)
        homeViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let { onChangedViewState(viewState) }
        }
    }

    private fun onChangedViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {

            is HomeViewModel.HomeViewState.Error -> {
                showToast(message = viewState.errorMessage)
            }

            is HomeViewModel.HomeViewState.AddBookmarkItem -> {
                showToast(message = "즐겨찾기가 추가되었습니다.")
            }

            is HomeViewModel.HomeViewState.DeleteBookmarkItem -> {
                showToast(message = "즐겨찾기가 제거되었습니다.")
            }
        }
    }

    private fun initUi() {
        val list = listOf(MapFragment(), BookmarkFragment())

        val pagerAdapter = FragmentPagerAdapter(list, this)


        with(binding) {
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 3
            viewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MapFragment.REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {

            when {
                grantResults.isEmpty() -> {
                    showToast(message = "권한이 없습니다.")
                }

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    showToast(message = "권한이 허용되었습니다.")
                    homeViewModel.permissionGrant()
                }

                else -> {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID,
                        null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backWait >= LIMIT_TIME) {
            backWait = System.currentTimeMillis()
            showToast(message = "뒤로가기 버튼을 한번 더 누르면 종료됩니다.")
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val INIT_TIME = 0L
        private const val LIMIT_TIME = 2000

    }
}

class FragmentPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = fragmentList.size
    override fun createFragment(position: Int) = fragmentList[position]

}