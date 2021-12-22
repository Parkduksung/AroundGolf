package com.rsupport.rv.viewer.sdk.data.response;

import com.rsupport.rscommon.parser.annotation.XMLNodeName;

/**
 * Created by hyosang on 2017. 8. 30..
 */

public class AgentConnectOptionResponse extends BaseResponse {
    @XMLNodeName("NETWORKMODE")
    public String networkMode;

    @XMLNodeName("CONTROLMODE")
    public String controlMode;

    @XMLNodeName("SCREENCOLOR")
    public String screenColor;

    @XMLNodeName("AUTOBLANKSCREEN")
    public String autoBlankScreen;

    @XMLNodeName("AUTOSYSTEMLOCK")
    public String autoSystemLock;

    @XMLNodeName("SESSIONSSL")
    public String sessionSsl;
}
