package com.rsupport.rv.viewer.sdk.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.rsupport.rv.viewer.sdk.common.log.RLog;

/**
 * <pre>
 * 미디어 스캔시 static method인 MediaScannerConnection.scanFile()을 사용하면 갤러리 앱에 표시되지 않음.
 * 인스턴스 생성하여 connect() 후 처리해야 갤러리에 표시됨.
 * </pre>
 */

public class MediaScanner {
    public interface IMediaScannerListener {
        void onScanCompleted(String path);
    }

    public static void scan(Context context, String path, IMediaScannerListener listener) {
        new MediaScanner(context, path, listener);
    }

    private String scanPath;
    private MediaScannerConnection mediaScannerConnection;
    private IMediaScannerListener listener = null;

    private MediaScanner(Context context, String path, IMediaScannerListener listener) {
        this.scanPath = path;
        this.listener = listener;

        mediaScannerConnection = new MediaScannerConnection(context, connectionClient);
        mediaScannerConnection.connect();
    }

    private MediaScannerConnection.MediaScannerConnectionClient connectionClient = new MediaScannerConnection.MediaScannerConnectionClient() {
        @Override
        public void onMediaScannerConnected() {
            RLog.d("MediaScanner Connected");
            mediaScannerConnection.scanFile(scanPath, null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            RLog.d("MediaScanner scan completed: " + path);
            if(listener != null) {
                listener.onScanCompleted(path);
            }

            mediaScannerConnection.disconnect();
        }
    };
}
