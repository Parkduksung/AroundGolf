package com.example.aroundgolf.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.aroundgolf.base.BaseViewModel
import com.example.aroundgolf.base.ViewState
import com.example.aroundgolf.data.repo.GolfRepository
import com.example.aroundgolf.ext.ioScope
import com.example.aroundgolf.room.GolfEntity
import com.example.aroundgolf.utils.GpsTracker
import com.example.aroundgolf.utils.Result
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import org.koin.java.KoinJavaComponent.inject

class MapViewModel(app: Application) : BaseViewModel(app) {

    val currentCenterMapPoint = MutableLiveData<MapPoint>()

    val currentZoomLevel = MutableLiveData<Int>()

    private val gpsTracker = GpsTracker(app)

    private val golfRepository by inject<GolfRepository>(GolfRepository::class.java)

    fun checkBookmarkState(itemName: String) {
        ioScope {
            when (val result = golfRepository.getGolfEntity(itemName)) {
                is Result.Success -> {
                    viewStateChanged(MapViewState.BookmarkState(result.data.like))
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error("checkBookmarkState Error"))
                }
            }
        }
    }

    fun toggleBookmarkItem(itemName: String, isBookmark: Boolean) {
        ioScope {
            when (val result = golfRepository.getGolfEntity(itemName)) {
                is Result.Success -> {
                    if (isBookmark) {
                        viewStateChanged(MapViewState.AddBookmarkItem(result.data))
                    } else {
                        viewStateChanged(MapViewState.DeleteBookmarkItem(result.data))
                    }
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error("toggleBookmarkItem Error"))
                }
            }
        }
    }

    fun setCurrentLocation() {
        ioScope {
            viewStateChanged(MapViewState.ShowProgress)
            when (val result = gpsTracker.getLocation()) {
                is Result.Success -> {
                    result.data.addOnCompleteListener { task ->

                        val location = task.result

                        val resultMapPoint =
                            MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)

                        viewStateChanged(MapViewState.SetCurrentLocation(resultMapPoint))
                        viewStateChanged(MapViewState.HideProgress)
                        viewStateChanged(MapViewState.SetZoomLevel(4))
                    }
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error(result.exception.message.toString()))
                    viewStateChanged(MapViewState.HideProgress)
                }
            }
        }
    }


    fun search() {
        viewStateChanged(MapViewState.ShowProgress)
        ioScope {
            when (val result = golfRepository.getAll()) {
                is Result.Success -> {

                    ioScope {
                        val campingItemList = mutableSetOf<MapPOIItem>()

                        val resultList = result.data

                        viewStateChanged(MapViewState.SetZoomLevel(8))

                        resultList.forEach { item ->
                            val mapPOIItem = MapPOIItem().apply {
                                itemName = item.name
                                mapPoint =
                                    MapPoint.mapPointWithGeoCoord(
                                        item.lat,
                                        item.log
                                    )
                                markerType = MapPOIItem.MarkerType.RedPin
                            }
                            campingItemList.add(mapPOIItem)
                        }
                        viewStateChanged(MapViewState.GetGolfItems(campingItemList.toTypedArray()))
                        viewStateChanged(MapViewState.HideProgress)
                    }
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error("GetDataError"))
                }
            }
        }
    }

    fun getSelectPOIItemInfo(itemName: String) {
        ioScope {
            when (val result = golfRepository.getGolfEntity(itemName)) {
                is Result.Success -> {
                    viewStateChanged(MapViewState.GetSelectPOIItem(result.data))
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error("getSelectPOIItemInfo Error"))
                }
            }
        }
    }

    sealed class MapViewState : ViewState {
        data class SetZoomLevel(val zoomLevel: Int) : MapViewState()
        data class SetCurrentLocation(val mapPoint: MapPoint) : MapViewState()
        data class GetGolfItems(val items: Array<MapPOIItem>) : MapViewState()

        data class GetSelectPOIItem(val item: GolfEntity) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
        object ShowProgress : MapViewState()
        object HideProgress : MapViewState()
        data class BookmarkState(val isChecked: Boolean) : MapViewState()
        data class AddBookmarkItem(val item: GolfEntity) : MapViewState()
        data class DeleteBookmarkItem(val item: GolfEntity) : MapViewState()
    }

}