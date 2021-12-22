package com.rsupport.rv.viewer.sdk.decorder.scap;

import com.rsupport.rv.viewer.sdk.decorder.model.IModel;


public class scapMsg implements IModel {

    public short type;
    public short data;

    public scapEncMsg EncInv;
    public scapSrnToSrnMsg SrnToSrn;
    public scapSrnToSrnMsg Scroll;
    public scapTileCacheMsg TileCache;
    public scapCursorMsg Cursor;
    public scapColorMapMsg ColorMap;
    public scapDebugMsg Debug;
    public scapNotifyMsg Notify;

    public scapCursorPosMsg CursorPos;

    public scapMsg() {
        EncInv = new scapEncMsg();
        SrnToSrn = new scapSrnToSrnMsg();
        Scroll = new scapSrnToSrnMsg();
        TileCache = new scapTileCacheMsg();
        Cursor = new scapCursorMsg();
        ColorMap = new scapColorMapMsg();
        Debug = new scapDebugMsg();
        Notify = new scapNotifyMsg();

        CursorPos = new scapCursorPosMsg();
    }

    public void save(byte[] szBuffer, int nStart) {
        int nIndex = nStart;

        type = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        data = (short) ((int) szBuffer[nIndex] & 0xff);
        nIndex++;
        EncInv.save(szBuffer, nIndex);
        nIndex += EncInv.size();
        SrnToSrn.save(szBuffer, nIndex);
        nIndex += SrnToSrn.size();
        Scroll.save(szBuffer, nIndex);
        nIndex += Scroll.size();
        TileCache.save(szBuffer, nIndex);
        nIndex += TileCache.size();
        Cursor.save(szBuffer, nIndex);
        nIndex += Cursor.size();
        ColorMap.save(szBuffer, nIndex);
        nIndex += ColorMap.size();
        Debug.save(szBuffer, nIndex);
        nIndex += Debug.size();
        Notify.save(szBuffer, nIndex);
        nIndex += Notify.size();

    }

    public void push(byte[] szBuffer, int start) {
        // TODO Auto-generated method stub
    }

    public int size() {
        return 2 + EncInv.size() + SrnToSrn.size() + Scroll.size() +
                TileCache.size() + Cursor.size() + ColorMap.size() + Debug.size() + Notify.size();
    }


    public void save2(byte[] szBuffer, int start, int dstOffset, int dstLen) {
        // TODO Auto-generated method stub
    }
}
