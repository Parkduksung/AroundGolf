package com.rsupport.rv.viewer.sdk.decorder.model;


// 참고할만한 내용>
// http://www.google.com/codesearch?hl=en&q=+java+CreateDIBSection+show:mZRIc-7-vKI:0A7-CYA6wqc:mYDGGrBGxgo&sa=N&cd=6&ct=rc&cs_p=http://www.mirrorservice.org/sites/master.us.finkmirrors.net/distfiles/jogl-1.1.1-src.zip&cs_f=jogl/src/net/java/games/jogl/impl/windows/WindowsOffscreenGLContext.java#first
// http://www.google.com/codesearch?hl=en&q=show:mYkLgbuJwpE:Y11WTzgFCjI:gHhqAOIPSOI&sa=N&ct=rd&cs_p=http://prdownloads.sourceforge.net/jfreereport/pixie-0.8.6.zip&cs_f=pixie-0.8.6/source/org/jfree/pixie/wmf/records/MfCmdDibBitBlt.java&start=1
// http://www.google.com/codesearch?hl=en&q=+java+CreateDIBSection+show:SjiP8mXfxYk:q8WXIcxiGyQ:LzCSS9sML5M&sa=N&cd=2&ct=rc&cs_p=ftp://sources.redhat.com/pub/rhug/swt-gcj-green-shapshot.tar.gz&cs_f=swt/upstream/org.eclipse.swt/Eclipse+SWT/win32/org/eclipse/swt/graphics/Image.java#first
// http://d.hatena.ne.jp/freebeans/20060928/p2



public class DIBINFO {
//	BITMAPINFOHEADER bmiHeader;
//
//	union
//	{
//		COLOR_MASK	bmiMask;
//		RGBQUAD		bmiColors[256];
//	};
	
	public BITMAPINFOHEADER bmiHeader ;
	public COLOR_MASK bmiMask;
	public RGBQUAD	bmiColors[];
	
	public DIBINFO() {
		bmiHeader = new BITMAPINFOHEADER();
		bmiMask = new COLOR_MASK();
		bmiColors = new RGBQUAD[256];
		for(int i=0; i<bmiColors.length; i++) {
			bmiColors[i] = new RGBQUAD();
		}
	} 
}
