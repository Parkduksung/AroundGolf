package com.rsupport.rv.viewer.sdk.data.response;


import com.rsupport.rscommon.define.RSErrorCode;
import com.rsupport.rscommon.parser.annotation.JsonKey;
import com.rsupport.rscommon.parser.annotation.XMLNodeName;
import com.rsupport.rscommon.parser.data.ParserDataModel;
import com.rsupport.rscommon.util.StringUtil;

/**
 * Created by hyosang on 2017. 7. 4..
 */

public class BaseResponse extends ParserDataModel {
    public static final String RVOEMTYPE_EXPLORER_DISABLE = "7";
    public static final int NOT_DEFINED = -1;

    @JsonKey("resultCode")
    @XMLNodeName("RETCODE")
    public String retcode = null;

    @JsonKey("message")
    @XMLNodeName("ERRORMSG")
    public String errorMessage = "";

    @JsonKey("debugMessage")
    @XMLNodeName("DEBUG_MESSAGE")
    public String debugMessage = "";

    //retcode 적용 전 사용하던 결과코드인듯.
    @XMLNodeName("RESULT")
    public String result = null;

    @JsonKey("detCode")
    @XMLNodeName("DETCODE")
    public String detCode = "";

    @JsonKey("detailMessage")
    @XMLNodeName("DETAIL_MESSAGE")
    public String detailMessage = "";

    @JsonKey("resultMessage")
    public String resultMessage = "";       //Json response only

    public boolean isNormalResponse() {
        if(retcode != null) {
            //RETCODE는 100이 정상
            return retcode.equals("100");
        }else if(result != null) {
            //RESULT 는 0이 정상
            return result.equals("0");
        }

        return false;
    }

    public int getRetCode() {
        if(retcode != null) {
            return StringUtil.parseInt(retcode, RSErrorCode.Network.NO_RETCODE);
        }else if(result != null) {
            return StringUtil.parseInt(result, RSErrorCode.Network.NO_RETCODE);
        }

        return RSErrorCode.Network.NO_RETCODE;
    }
}
