package com.rsupport.rv.viewer.sdk.setting


import com.rsupport.rv.viewer.sdk.common.RuntimeData
import com.rsupport.rv.viewer.sdk.constant.MODE_XENC


fun isXenc() = RuntimeData.getInstance().agentConnectOption.viewerMode == MODE_XENC