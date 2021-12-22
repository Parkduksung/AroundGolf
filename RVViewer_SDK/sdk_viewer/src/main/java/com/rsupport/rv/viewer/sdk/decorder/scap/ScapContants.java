package com.rsupport.rv.viewer.sdk.decorder.scap;

public class ScapContants {
	
//	enum scapMsgId
//	{
//		scapNull, 
//		
//		// prevent timeout socket close.
//		scapChannelNop,
//
//		// screen capture engine state notification, inputs
//		scapNotify, scapOption, scapOption2, scapMouseInput, scapKeyInput,
//		scapCursorPos, scapCursorCached, scapCursorNew, 
//
//		// appshare
//
//		scapInitDesktop, 
//		scapColorMap, 
//		scapSrnRgn, 
//		scapEnc,
//		
//		scapInitChannel,
//
//		// update screen: expands with scapUpdateid
//		scapUpdate = 100, 
//		scapUpdateRequest,
//
//		scapMsgIdEnd,
//	};
	
	// enum scapMsgId 내용을 아래로 포팅
	
	public static final int scapNull = 0;
	// prevent timeout socket close.
	public static final int scapChannelNop = 1;
	
	// screen capture engine state notification, inputs
	public static final int scapNotify = 2;
	public static final int scapOption = 3;
	public static final int scapOption2 = 4;
	public static final int scapMouseInput = 5;
	public static final int scapKeyInput = 6;
	public static final int scapCursorPos = 7;
	public static final int scapCursorCached = 8;
	public static final int scapCursorNew = 9;
	
	// appshare
	public static final int scapInitDesktop = 10;
	public static final int scapColorMap = 11;
	public static final int scapSrnRgn = 12;
	public static final int scapEnc = 13;
	public static final int scapInitChannel =14;
	
	// update screen: expands with scapUpdateid
	public static final int scapUpdate = 100;
	public static final int scapUpdateRequest = 101;
	public static final int scapMsgIdEnd = 102;
	
	
//	enum scapUpdateid
//	{
//		// screen capture engine state notification
//		scapUpdateNull = scapMsgIdEnd, 
//		scapUpdateNotify, 
//
//		scapStream,
//
//		scapRegion, 
//		scapClips,
//
//		scapSrnSave,
//
//		scapSrnToSrn,
//		scapGraphyCache,
//		scapTileCache,
//		scapSeamless,
//
//		scapBitmap, scapBrush,
//
//		scapBlock, // BLOCK RECT
//
//		scapDebug,
//
//		// screen capture engine state notification
//	};
	
	
	// enum scapUpdateid 내용을 포팅함
	public static final int scapUpdateNull = 102;
	public static final int scapUpdateNotify = 103;
	public static final int scapStream = 104;
	public static final int scapRegion = 105;
	public static final int scapClips = 106;
	
	public static final int scapSrnSave = 107;
	public static final int scapSrnToSrn = 108;
	public static final int scapGraphyCache = 109;
	public static final int scapTileCache = 110;
	public static final int scapSeamless = 111;
	
	public static final int scapBitmap = 112;
	public static final int scapBrush = 113;
	public static final int scapBlock = 114;
	public static final int scapDebug = 115;
	
//	enum {
//		tc_add = 'A',
//		tc_padd = 'a',
//		tc_rmv = 'r',
//		tc_hit = 'H',
//		tc_phit = 'h'
//	};
	
	public static final char tc_add = 'A';
	public static final char tc_padd = 'a';
	public static final char tc_rmv = 'r';
	public static final char tc_hit = 'H';
	public static final char tc_phit = 'h';	
}
