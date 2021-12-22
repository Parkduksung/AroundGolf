package com.rsupport.rv.viewer.sdk.util

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapRegionDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PictureUtils {
    private val delegate: PictureStore by lazy {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) PictureStoreImpl()
        else PictureStoreQ()
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    fun store(context: Context, pictureByteArray: ByteArray, completed: (Uri) -> Unit) {
        requireNotNull(BitmapRegionDecoder.newInstance(pictureByteArray, 0, pictureByteArray.count(), true)){
            "The pictureByteArray fail to decode."
        }

        delegate.store(context, pictureByteArray, completed)
    }
}

interface PictureStore{
    fun store(context: Context, jpgBytes: ByteArray, completed: (Uri) -> Unit)
}

class PictureStoreImpl: PictureStore {
    override fun store(context: Context, jpgBytes: ByteArray, completed: (Uri) -> Unit){
        val filename = DeviceUtil.getScreenShotSavePath()
        FileOutputStream(filename).use {
            it.write(jpgBytes)
            it.flush()
        }
        MediaScanner.scan(context, filename) { path -> //start gallery activity
            completed(Uri.fromFile(File(filename)))
        }
    }
}

class PictureStoreQ: PictureStore {
    override fun store(context: Context, jpgBytes: ByteArray, completed: (Uri) -> Unit) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, DeviceUtil.createPictureFileName())
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val collection = MediaStore.Images.Media
                .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        resolver.insert(collection, values)?.also { item ->
            resolver.openFileDescriptor(item, "w", null).use { pfd ->
                FileOutputStream(pfd?.fileDescriptor).use {
                    it.write(jpgBytes)
                }
            }
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(item, values, null, null)
            completed(item)
        }
    }
}