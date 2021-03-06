/********************************************************************************
*       ______   _____    __    __ _____   _____   _____    ______  _______
*      / ___  | / ____|  / /   / // __  | / ___ | / __  |  / ___  ||___  __|
*     / /__/ / | |____  / /   / // /  | |/ /  | |/ /  | | / /__/ /    / /
*    / ___  |  |____  |/ /   / // /__/ // /__/ / | |  | |/ ___  |    / /
*   / /   | |   ____| || |__/ //  ____//  ____/  | |_/ // /   | |   / /
*  /_/    |_|  |_____/ |_____//__/    /__/       |____//_/    |_|  /_/
*
********************************************************************************
*
* Copyright (c) 2013 RSUPPORT Co., Ltd. All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains the property
* of RSUPPORT Company Limited and its suppliers, if any. The intellectual
* and technical concepts contained herein are proprietary to RSUPPORT
* Company Limited and its suppliers and are protected by trade secret
* or copyright law. Dissemination of this information or reproduction
* of this material is strictly forbidden unless prior written permission
* is obtained from RSUPPORT Company Limited.
*
* FileName: PreferenceConstant.java
* Author  : "kyeom@rsupport.com"
* Date    : 2013. 1. 8.
* Purpose : Preference constants defined
*
* [History]
*
* 2013. 1. 8. -Initialize
*
*/
package com.rsupport.rv.viewer.sdk.setting;

/**
 * @author "kyeom@rsupport.com"
 *
 */
public class PreferenceConstant {
	
	/** Preference, Bundle item's name **/
	public static final String RV_TRUE					= "true";
	public static final String RV_FALSE					= "false";
	public static final String RV_AGENTNAME				= "agentname";
	public static final String RV_GROUPID				= "groupid";
	public static final String RV_GROUPNAME				= "groupname";
	public static final String RV_ID					= "id";
	public static final String RV_PWD					= "pwd";
	public static final String RV_PARENTNAME			= "PARENTNAME";
	public static final String RV_PARENTPOSITION		= "PARENT_POSITION";
	public static final String RV_PARENTID				= "PARENTID";
	public static final String RV_REFRESH				= "REFRESH";
	public static final String RV_RELOAD				= "RELOAD";

	/** Setting Info item **/
	public static final String RV_SERVER_TYPE			= "producttype";
	public static final String RV_SERVER_IP_PERSONAL	= "serverip_personal";
	public static final String RV_SERVER_IP_CORP		= "serverip_corp";
	public static final String RV_SERVER_IP_SERVER		= "serverip_server";
	public static final String RV_SERVER_CORP_ID		= "corpid";
	
	/** Agent Info (& AgentList Info) **/
	public static final String RV_AGENT_GUID			= "agentguid";
	
	/** Resolution **/
	public static final String RV_RESOLUTION_WIDTH		= "resolution_width";
	public static final String RV_RESOLUTION_HEIGHT		= "resolution_heigth";
	public static final String RV_RESOLUTION_COLORBIT	= "resolution_colorbit";
	
	/** Common **/
	public static final String RV_BUNDLE_ACTIVITY_BACK	= "BACKPAGE";
	public static final String RV_STATE_STATED			= "isstart";
	public static final String MEMBER_JOIN_USER_ID		= "USERID";

	/**
	 * ?????? ?????? ?????? ?????????
	 * **/
	public static final String PREF_CONTROL_CONFIG      = "user_control_config";

	public static final String KEY_SCREEN_LOCK 			= "screen_lock";

	public static final String KEY_CONTROL_MODE	 		= "control_mode";
	public static final int CONTROL_MODE_MOUSE 			= 1;
	public static final int CONTROL_MODE_TOUCH 			= 2;

	public static final String KEY_SCREEN_MODE 			= "screen_mode";
	public static final int SCREEN_MODE_FIT 			= 1;
	public static final int SCREEN_MODE_RESET 			= 2;

	public static final String KEY_SHARE_SOUND 			= "share_sound";
	public static final String KEY_SCROLL_BUTTON 		= "scroll_button";
}
