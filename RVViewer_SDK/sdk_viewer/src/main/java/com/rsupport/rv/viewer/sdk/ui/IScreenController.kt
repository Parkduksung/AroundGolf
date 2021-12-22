package com.rsupport.rv.viewer.sdk.ui

interface IScreenController {
    var isDrawingRun: Boolean

    fun setSourceSize(width: Int, height: Int)
    fun setRemoteSize(width: Int, height: Int)
    fun moveScreen(x: Int, y: Int)

    fun isScrollable(direction: Direction): Boolean

    fun setClickPos(viewX: Int, viewY: Int)

    fun getRealPosX(viewX: Int): Int
    fun getRealPosY(viewY: Int): Int

    fun isValidPosition(x: Int, y: Int): Boolean

    fun zoomReset()
    fun fitToScreen()


    fun startDrag(x: Int, y: Int)
    fun dragMove(x: Int, y: Int)
    fun endDrag(x: Int, y: Int)

    fun getViewWidth(): Int
    fun getViewHeight(): Int

    fun zoomBegin()
    fun zoom(scaleFactor: Float)

    fun videoOptionChanged()

    fun render(data: ByteArray, length: Int)

    fun setRenderKeyFrame()
    fun saveScreenCtrl(data: ByteArray, length: Int)
    fun saveHXSoundInfo(hxSoundInfo: ByteArray)
    fun release()

    fun setOnRendererListener(onRendererListener: OnRendererListener)

    fun setVisibleHeight(height: Int)

    interface OnRendererListener{
        /**
         * 렌더링이 시작되면 호출된다.
         */
        fun onStart()
    }

    enum class Direction{
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
