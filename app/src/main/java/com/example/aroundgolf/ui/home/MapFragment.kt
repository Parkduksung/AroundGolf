package com.example.aroundgolf.ui.home

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.aroundgolf.R
import com.example.aroundgolf.base.BaseFragment
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.databinding.MapFragmentBinding
import com.example.aroundgolf.ext.hasPermission
import com.example.aroundgolf.ext.hidePOIInfoContainer
import com.example.aroundgolf.ext.showPOIInfoContainer
import com.example.aroundgolf.ext.showToast
import com.example.aroundgolf.viewmodel.HomeViewModel
import com.example.aroundgolf.viewmodel.MapViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<MapFragmentBinding>(R.layout.map_fragment) {

    private val campingItemList = mutableSetOf<MapPOIItem>()

    private val mapViewModel by viewModel<MapViewModel>()

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var currentLocation: MapPOIItem

    private val mapViewEventListener =
        object : MapView.MapViewEventListener {
            override fun onMapViewInitialized(p0: MapView?) {

            }

            override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
                mapViewModel.currentCenterMapPoint.value = p1
            }

            override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
                mapViewModel.currentZoomLevel.value = p1
            }

            override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
                binding.containerPoiInfo.hidePOIInfoContainer(requireContext())
            }

            override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationRequest()
        initViewModel()

    }

    private val poiItemEventListener = object : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            p1?.let { item ->
                if (item.mapPoint != currentLocation.mapPoint) {
                    mapViewModel.getSelectPOIItemInfo(item.itemName)
                    mapViewModel.checkBookmarkState(item.itemName)

                    binding.itemBookmark.setOnClickListener {
                        mapViewModel.toggleBookmarkItem(
                            item.itemName, binding.itemBookmark.isChecked
                        )
                    }
                }
            }
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

        }
    }

    private fun initUi() {
        binding.containerMap.setMapViewEventListener(this@MapFragment.mapViewEventListener)
        binding.containerMap.setPOIItemEventListener(this@MapFragment.poiItemEventListener)

        mapViewModel.setCurrentLocation()
    }

    private fun initViewModel() {
        binding.viewModel = mapViewModel

        mapViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? MapViewModel.MapViewState)?.let { onChangedMapViewState(viewState) }
        }

        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let { onChangedHomeViewState(viewState) }
        }
    }


    private fun setCurrentLocation(currentMapPoint: MapPoint) {
        if (::currentLocation.isInitialized) {
            binding.containerMap.removePOIItem(currentLocation)
        }

        currentLocation = MapPOIItem().apply {
            itemName = "Current Location!"
            mapPoint = currentMapPoint
        }

        with(binding.containerMap) {
            addPOIItem(currentLocation)
            setMapCenterPoint(currentMapPoint, false)
        }
    }

    private fun onChangedHomeViewState(viewState: ViewState) {

        when (viewState) {
            is HomeViewModel.HomeViewState.AddBookmarkItem -> {
                binding.itemBookmark.isChecked = true
            }
            is HomeViewModel.HomeViewState.DeleteBookmarkItem -> {
                binding.itemBookmark.isChecked = false
            }

            is HomeViewModel.HomeViewState.PermissionGrant -> {
                initUi()
            }

        }
    }

    private fun onChangedMapViewState(viewState: ViewState) {
        when (viewState) {
            is MapViewModel.MapViewState.SetCurrentLocation -> {
                setCurrentLocation(viewState.mapPoint)
            }

            is MapViewModel.MapViewState.GetGolfItems -> {
                campingItemList.addAll(viewState.items)
                binding.containerMap.removeAllPOIItems()
                binding.containerMap.addPOIItems(viewState.items)
            }

            is MapViewModel.MapViewState.SetZoomLevel -> {
                binding.containerMap.setZoomLevel(viewState.zoomLevel, true)
            }

            is MapViewModel.MapViewState.Error -> {
                showToast(message = viewState.errorMessage)
            }

            is MapViewModel.MapViewState.GetSelectPOIItem -> {
                with(binding) {
                    containerPoiInfo.showPOIInfoContainer(requireContext())
                    itemName.text = viewState.item.name
                    itemLocation.text = viewState.item.address
                }
            }

            is MapViewModel.MapViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is MapViewModel.MapViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }


            is MapViewModel.MapViewState.BookmarkState -> {
                binding.itemBookmark.isChecked = viewState.isChecked
            }

            is MapViewModel.MapViewState.AddBookmarkItem -> {
                homeViewModel.addBookmarkItem(viewState.item)
            }

            is MapViewModel.MapViewState.DeleteBookmarkItem -> {
                homeViewModel.deleteBookmarkItem(viewState.item)
            }

        }
    }

    private fun locationRequest() {
        val permissionApproved =
            requireActivity().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionApproved) {
            initUi()
        } else {
            val provideRationale = shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            if (provideRationale) {
                initUi()
            } else {
                requireActivity().requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
    }

}
