package com.rsupport.rv.viewer.sdk.constant

import com.rsupport.rv.viewer.sdk_viewer.R

/**
 * tutorial type
 */
const val TYPE_TUTORIAL = "typeTutorial"
const val TYPE_TUTORIAL_BASIC = 0
const val TYPE_TUTORIAL_CONTROL_TOUCH = 1
const val TYPE_TUTORIAL_CONTROL_MOUSE = 2

/**
 * api url
 */
const val URL_BASE_STAP = "https://stap.rview.com:443"
const val URL_BASE_BASIC = "https://www.rview.com:443"
const val URL_APP_INFO = "/remoteview/command/app/appinfo.aspx"

/**
 * types
 */
const val TYPE_AGENT_TITLE = 0
const val TYPE_AGENT_GROUP = 1
const val TYPE_AGENT_NORMAL = 2

const val ENGINE_CAPTURE = 0
const val ENGINE_HX = 1

/**
 * module type
 */
const val MODE_VRVD = "vrvd"
const val MODE_XENC = "xenc"

/**
 * ret code
 */
const val SUCCESS = "100"
const val FAIL = "0"
const val ERR_INVALID_PARAMETER = 111
const val ERR_NOT_FOUND_USERID = 112
const val ERR_NOT_FOUND_AGENTID = 113
const val ERR_INVALID_USER_ACCOUNT = 114
const val ERR_ALREADY_USINGSESSION = 115
const val ERR_BLOCK_MOBILELOGIN = 116
const val ERR_INVITE_EXPIRED = 120
const val ERR_INVITE_ALREADY = 121
const val ERR_APP_VERSION = 130
const val ERR_INVAILD_COMPANYID = 131
const val ERR_NEED_SWITCH_MEMBER = 132
const val ERR_NEED_UPGRADE_MEMBER = 133
const val ERR_AES_NOT_FOUND_USERID = 140
const val ERR_AES_INVALID_USER_ACCOUNT = 141
const val ERR_USER_ACCOUNT_LOCK = 142
const val ERR_OTP_AUTH_FAIL = 145
const val ERR_WOL_NOT_FOUND_AGENT = 146
const val ERR_ARP_NOT_FOUND_AGENT = 147
const val ERR_ALREADY_SAME_WORKING = 211
const val ERR_ALREADY_DELETE_AGENTID = 212
const val ERR_AGENT_NOT_LOGIN = 213
const val ERR_ONLY_WEBSETUP = 214
const val ERR_AGENT_EXPIRED = 215
const val ERR_INVALID_MACADDRESS = 300
const val ERR_INVALID_LOCALIP = 301
const val ERR_INVALID_ROLE = 310
const val ERR_LIC_EXPIRED = 405
const val ERR_LIC_SERVICE_ERROR = 406
const val ERR_ACTIVE = 413
const val ERR_UNAUTH_MAC_ADDRESS = 600
const val ERR_UNAUTH_DEVICE = 601
const val ERR_UNAUTH_USER = 700
const val ERR_UNAUTH_PASSWORD_FAIL = 701
const val ERR_PASSWORD_EXPIRED = 702
const val ERR_DIRECT_EXEC_EXPIRED = 704
const val ERR_OTP_AUTH_FAILED = 826
const val ERR_OTP_KEY_INVALID = 840
const val ERR_SERVICE_LIMIT_TIME = 846
const val ERR_NO_EMAIL = 850
const val ERR_ADMINISTRATORS_LIMIT_CONNECT = 852
const val ERR_SQL_ERROR = 911

fun getErrorMessage(resultCode: Int) =
        when (resultCode) {
            ERR_INVALID_PARAMETER -> R.string.weberr_invalid_parameter
            ERR_NOT_FOUND_USERID -> R.string.weberr_invalid_user_account
            ERR_INVALID_USER_ACCOUNT -> R.string.weberr_invalid_user_account
            ERR_AES_INVALID_USER_ACCOUNT -> R.string.weberr_invalid_user_account
            ERR_AES_NOT_FOUND_USERID -> R.string.weberr_invalid_user_account
            ERR_NOT_FOUND_AGENTID -> R.string.weberr_not_found_agentid
            ERR_ALREADY_USINGSESSION -> R.string.weberr_aleady_usingsession
            ERR_BLOCK_MOBILELOGIN -> R.string.weberr_block_mobilelogin
            ERR_INVITE_EXPIRED -> R.string.weberr_invite_expired
            ERR_INVITE_ALREADY -> R.string.weberr_invite_already
            ERR_ALREADY_SAME_WORKING -> R.string.weberr_already_same_working
            ERR_ALREADY_DELETE_AGENTID -> R.string.weberr_already_delete_agentid
            ERR_AGENT_NOT_LOGIN -> R.string.weberr_agent_not_login
            ERR_ONLY_WEBSETUP -> R.string.weberr_only_websetup
            ERR_AGENT_EXPIRED -> R.string.weberr_agent_expired
            ERR_SQL_ERROR -> R.string.weberr_sql_error
            ERR_APP_VERSION -> R.string.weberr_app_version
            ERR_INVAILD_COMPANYID -> R.string.weberr_invalid_companyid
            ERR_NEED_SWITCH_MEMBER -> R.string.weberr_need_switch_member
            ERR_NEED_UPGRADE_MEMBER -> R.string.weberr_need_update_member
            ERR_WOL_NOT_FOUND_AGENT -> R.string.weberr_wol_not_found_agent
            ERR_INVALID_MACADDRESS -> R.string.weberr_invalid_macaddress_localip
            ERR_INVALID_LOCALIP -> R.string.weberr_invalid_macaddress_localip
            ERR_LIC_EXPIRED -> R.string.weberr_lic_expired
            ERR_LIC_SERVICE_ERROR -> R.string.weberr_lic_service_error
            ERR_INVALID_ROLE -> R.string.weberr_invalid_role
            ERR_UNAUTH_MAC_ADDRESS -> R.string.weberr_unauth_macaddress
            ERR_UNAUTH_DEVICE -> R.string.weberr_unauth_device
            ERR_USER_ACCOUNT_LOCK -> R.string.weberr_unauth_user
            ERR_ARP_NOT_FOUND_AGENT -> R.string.weberr_arp_not_found_woldevice
            ERR_DIRECT_EXEC_EXPIRED -> R.string.msg_direct_exec_input_expired
            ERR_ACTIVE -> R.string.weberr_active_fail
            ERR_OTP_AUTH_FAIL -> R.string.weberr_otp_auth_failed
            ERR_OTP_KEY_INVALID -> R.string.weberr_otp_not_auth
            ERR_SERVICE_LIMIT_TIME -> R.string.weberr_time_limit_used
            ERR_ADMINISTRATORS_LIMIT_CONNECT -> R.string.weberr_connect_limit_administrators
            ERR_NO_EMAIL -> R.string.msg_emailauth_no_email
            else -> R.string.msg_unablenetwork
        }