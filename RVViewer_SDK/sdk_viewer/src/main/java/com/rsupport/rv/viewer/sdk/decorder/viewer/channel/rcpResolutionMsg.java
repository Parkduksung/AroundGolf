package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;



import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.model.IModel;


public class rcpResolutionMsg implements IModel {
	
	public int counts = 0;
	public int currentmodeindex = 0;
	public rcpResolutionInfoMsg[] resolutionInfoMsg = null;

	public void push(byte[] szBuffer, int nStart) {}

	public void save(byte[] szBuffer, int nStart) {
		byte[] bCounts = new byte[1];
		byte[] bCurrentModeIndex = new byte[1];
	
		System.arraycopy(szBuffer, 0, bCounts, 0, 1);
		counts |= bCounts[0] & 0xff;
//		counts = (int)bCounts[0];
		RLog.d (Integer.toBinaryString(counts));
		
		System.arraycopy(szBuffer, 1, bCurrentModeIndex, 0, 1);
		currentmodeindex = (int)bCurrentModeIndex[0];

		RLog.d("resolutionInfoMsg count : " + counts);
		resolutionInfoMsg = new rcpResolutionInfoMsg[counts];
		int bStart = 2;
		for (int i = 0 ; i < counts ; i++) {
			resolutionInfoMsg[i] = new rcpResolutionInfoMsg();
			resolutionInfoMsg[i].save(szBuffer, bStart);
			bStart += 5;
		}
	}

	public void save2(byte[] szBuffer, int nStart, int dstOffset, int dstLen) {}

	public int size() {
		return 2 + counts * resolutionInfoMsg[0].size();
	}

}


//typedef struct _tagResolutionMsg
//{
//	RU8	counts;					// 변경 가능한 시스템 해상도 갯수 또는 1(해상도 변경 및 현재 해상도 얻어올 경우)
//	RU8	currentmodeindex;		// 전체 해상도 목록 중에서 현재 해상도의 인덱스 번호(0번부터 시작)
//	rcpResolutionInfoMsg	resInfo[1];
//} rcpResolutionMsg;
