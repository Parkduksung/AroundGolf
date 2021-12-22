package com.rsupport.rv.viewer.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.Define;
import com.rsupport.rv.viewer.sdk.common.Enums;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.pref.SettingPref;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


public class AgentInfo implements Parcelable {
	public static final int REAL_AGENT 		= 0;
	public static final int VIRTUAL_AGENT 	= 1;
	
	private final long PERMISSION_OK = 0;

	public String key;
	public String guid;
	public String name;
	public String osname;
	public int status = ComConstant.AGENT_STATUS_NOLOGIN;
	public String remoteip;
	public String localip;
	public String expired 	= ComConstant.AGENT_OK;
	public String extend 	= String.valueOf(ComConstant.RVFLAG_KEY_NONE);
	public String pflags;			//season 1 permission
	public String rvcfgs2;			//season 2 permission
	public String groupid;
	public String macaddr;
	public String logintime;
	public String regtime;
	public String userid;
	/** Enterprise used only, so personal unconditionally is 1. */
	public String isactive = "1"; 
	/** case of nateon (0 : pc, 1 : ios, 2 : win, 3 : android) */
	public String devicetype;
	public int 	  type;
	public String iswol = "0";
	public String isremote = "0";	//1:제어중,0:제어중아님
	public String delagentdisabled = "0";
	public String isremotestop = "0";	// 1: 세션종료가능, 0: 세션종료불가능
	public String subnetmask = "0.0.0.0";
	public String powerstate = "0"; // 0:꺼짐, 1:켜짐
	public String agentversion = "0.0.0.0";
	public String accessauthtype = "0"; // (0(기본):접근ID/PW 사용, 1; OTP, 2: WindowsLogon)
	public String useaccessauth = "1"; // 0:사용안함, 1(기본):사용함
	public String groupname = "";
	public boolean autoSystemLock = true;
	public boolean agentInputLock = false;	//원격제어중 에이전트 입력 잠금
	public boolean agentExecuteRestriction = false;	//원격지 PC 프로세스 서비스 중지,재실행 제한
	public int remoteEndAction = 0;		//원격 종료시 에이전트 처리. AutoSystemLock과 연계되는 값이어야 함.
	public int agentUrlBlock = 0;			//Agent URL 차단 사용여부
	public int useVideoControlMode = 0;	//동영상제어모드 사용여부
	public int sortNum = 0;
	/**
	 * 1 일 경우 비밀버호를 사용하지 않고 agent 삭제가 가능하다.(서버 옵션)
	 */
	public int noauthagentdelete = 0; // agent 삭제시 0 비밀번호 사용, 1 비밀번호 사용하지 않음

	public boolean getControlPermission() {
		return checkPermission(ComConstant.RVPOPTS_RCONTROL, ComConstant.RVPOPTS2_RCONTROL);
	}
	

	public boolean getCapturePermission() {
		return checkPermission(ComConstant.RVPOPTS_RSCAPTURE, ComConstant.RVPOPTS2_RCONTROL_CAPTURE);
	}
	
	public boolean getProcessPermission() {
		return checkPermission(ComConstant.RVPOPTS_RPROCESS, PERMISSION_OK);
	}
	
	public boolean getSystemPermission() {
		return checkPermission(ComConstant.RVPOPTS_RSYSTEM, PERMISSION_OK);
	}
	
	public boolean getVPNPermission() {
		return checkPermission(ComConstant.RVPOPTS_RVPN, ComConstant.RVPOPTS2_EXTEND_IVPN);
	}
	
	public boolean getVPROPermission() {
		if (extend != null && extend.trim().equals("1")) {
			return checkPermission(ComConstant.RVPOPTS_RVPRO, ComConstant.RVPOPTS2_EXTEND_VPRO);
		}
		return false;
	}
	
	public boolean getFilePermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPOPTS2_RCONTROL_FILE);
	}
	
	public boolean getPrintPermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPOPTS2_RCONTROL_RPRINT);
	}
	
	public boolean getCAMPermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPOPTS2_RCONTROL_CAM);
	}
	
	public boolean getClipPermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPORTS2_RCONTROL_CLIP);
	}

	public boolean getMobilePermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPOPTS2_EXTEND_MOBILE);
	}
	
	public boolean getVirtualPermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPOPTS2_EXTEND_VIRTUAL);
	}
	
	public boolean getLiveViewPermission() {
		return checkPermission(PERMISSION_OK, ComConstant.RVPORTS2_EXTEND_LIVEVIEW);
	}

	public boolean isAuthBypass() {
		if(Define.PROP_DISABLE.equals(useaccessauth)) {
			//인증처리 사용안함
			return true;
		}else if((Define.PROP_ENABLE.equals(useaccessauth)) && Define.AuthType.NOAUTH.equals(accessauthtype)) {
			//인증처리 사용함 & 인증방법 사용안함
			return true;
		}

		return false;
	}
	
	private boolean checkPermission(long s1PermConst, long s2PermConst) {

		if(SettingPref.getInstance().productType == Enums.ProductType.STANDARD) {
			return true;
		}
		if (rvcfgs2 != null && !rvcfgs2.trim().equals("")) {	
			if ((Long.parseLong(rvcfgs2.trim()) & s2PermConst) > 0 || s2PermConst == PERMISSION_OK) {
				return true;
			}
		} else if (pflags != null) {
			if (pflags.trim().equals("")) {//PRODUCT_PERSIONAL
				return true;
			} else if ((Long.parseLong(pflags.trim()) & s1PermConst) > 0 || s1PermConst == PERMISSION_OK) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkExplorerUsable() {
		return RuntimeData.getInstance().appProperty.isAppFtp();
	}
	
	private boolean checkExplorerOemType() {
		return !(RuntimeData.getInstance().appProperty.isRvOemTypeExplorerDisabled());
	}
	
	private boolean checkExplorerSupportDevice() {
		int extendConst = 0;
		try {
			extendConst = Integer.parseInt(extend.trim());
		} catch (NumberFormatException e) {
			return false;
		}
		switch (extendConst) {
		case ComConstant.RVFLAG_KEY_NONE: 	//General
		case ComConstant.RVFLAG_KEY_VPRO: 	//vPro
		case ComConstant.RVFLAG_KEY_RWT:  	//RWT
		case ComConstant.RVFLAG_KEY_HVS:	//HyperV Server
		case ComConstant.RVFLAG_KEY_HVI:	//HyperV Image
		case ComConstant.RVFLAG_KEY_VMI:	//VM Image
		case ComConstant.RVFLAG_KEY_VHDI:	//VirtualPC Image
		case ComConstant.RVFLAG_KEY_VBOX:	//VBox Image
		case ComConstant.RVFLAG_KEY_XGEN:	//XGen Image
//		case ComConstant.RVFLAG_KEY_MAC:	//MacOS X
			return true;

		case ComConstant.RVFLAG_KEY_UBUN:	//Linux - Ubuntu


			return false;
			
		default: 
			return false;
		}
	}
	
	public boolean checkVirtualDevice() {
		int extendConst = 0;
		try {
			extendConst = Integer.parseInt(extend.trim());
		} catch (NumberFormatException e) {
			Log.e("checkVirtualDevice", e.getLocalizedMessage());
			return false;
		}
		switch (extendConst) {
		case ComConstant.RVFLAG_KEY_VMI:	//VM Image
		case ComConstant.RVFLAG_KEY_VHDI:	//VirtualPC Image
		case ComConstant.RVFLAG_KEY_VBOX:	//VBox Image
		case ComConstant.RVFLAG_KEY_XGEN:	//XGen Image
			
			return true;
		default: 
			return false;
		}
	}

	public boolean isRemoteWOL() {
		if(GlobalStatic.WOL_MACHINE.equals(extend)) {
			return true;
		}

		return false;
	}

	public String getPCType() {
		int extendConst = 0;
		String pcType = "";
		try {
			extendConst = Integer.parseInt(extend);
		} catch (NumberFormatException e) {
			Log.e("getPCType", e.getLocalizedMessage());
			return pcType;
		}
		switch (extendConst) {
			case ComConstant.RVFLAG_KEY_NONE:
				pcType = "";// General
				break;
			case ComConstant.RVFLAG_KEY_VPRO:
				pcType = "vPro";
				break;
			case ComConstant.RVFLAG_KEY_RWT:
				pcType = "RWT";
				break;
			case ComConstant.RVFLAG_KEY_HVS:
				pcType = "HyperV Server";
				break;
			case ComConstant.RVFLAG_KEY_HVI:
				pcType = "HyperV Image";
				break;
			case ComConstant.RVFLAG_KEY_VMI:
				pcType = "VM Image";
				break;
			case ComConstant.RVFLAG_KEY_VHDI:
				pcType = "VirtualPC Image";
				break;
			case ComConstant.RVFLAG_KEY_VBOX:
				pcType = "VBox Image";
				break;
			case ComConstant.RVFLAG_KEY_XGEN:
				pcType = "XGen Image";
				break;
			case ComConstant.RVFLAG_KEY_UBUN:
				pcType = "Linux - Ubuntu";
				break;
			case ComConstant.RVFLAG_KEY_MAC:
				pcType = "MacOS X";
				break;
			case ComConstant.RVFLAG_KEY_REMOTEWOL:
				pcType = "RemoteWOL";
				break;
			case ComConstant.RVFLAG_KEY_HWWOL:
				pcType = "WOL PC";
				break;
			case ComConstant.RVFLAG_KEY_ANDROID:
				pcType = "Android";
				break;
			default:
				// unknown
		}
		return pcType;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("key=").append(key).append(",")
				.append("guid=").append(guid).append(",")
				.append("name=").append(name).append(",")
				.append("osname=").append(osname).append(",")
				.append("status=").append(status).append(",")
				.append("remoteip=").append(remoteip).append(",")
				.append("localip=").append(localip).append(",")
				.append("expired=").append(expired).append(",")
				.append("extend=").append(extend).append(",")
				.append("pflags=").append(pflags).append(",")
				.append("rvcfgs2=").append(rvcfgs2).append(",")
				.append("groupid=").append(groupid).append(",")
				.append("macaddr=").append(macaddr).append(",")
				.append("logintime=").append(logintime).append(",")
				.append("regtime=").append(regtime).append(",")
				.append("userid=").append(userid).append(",")
				.append("isactive=").append(isactive).append(",")
				.append("devicetype=").append(devicetype).append(",")
				.append("type=").append(type).append(",")
				.append("iswol=").append(iswol).append(",")
				.append("isremote=").append(isremote).append(",")
				.append("delagentdisabled=").append(delagentdisabled).append(",")
				.append("isremotestop=").append(isremotestop).append(",")
				.append("subnetmask=").append(subnetmask).append(",")
				.append("powerstate=").append(powerstate).append(",")
				.append("agentversion=").append(agentversion).append(",")
				.append("accessauthtype=").append(accessauthtype).append(",")
				.append("useaccessauth=").append(useaccessauth).append(",")
				.append("groupname=").append(groupname).append(",")
				.append("autoSystemLock=").append(autoSystemLock).append(",")
				.append("agentInputLock=").append(agentInputLock).append(",")
				.append("agentExecuteRestriction=").append(agentExecuteRestriction).append(",")
				.append("remoteEndAction=").append(remoteEndAction).append(",")
				.append("agentUrlBlock=").append(agentUrlBlock).append(",")
				.append("useVideoControlMode=").append(useVideoControlMode)
				.append("noauthagentdelete=").append(noauthagentdelete)
				;

		return sb.toString();
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(key);
		parcel.writeString(guid);
		parcel.writeString(name);
		parcel.writeString(osname);
		parcel.writeInt(status);
		parcel.writeString(remoteip);
		parcel.writeString(localip);
		parcel.writeString(expired);
		parcel.writeString(extend);
		parcel.writeString(pflags);
		parcel.writeString(rvcfgs2);
		parcel.writeString(groupid);
		parcel.writeString(macaddr);
		parcel.writeString(logintime);
		parcel.writeString(regtime);
		parcel.writeString(userid);
		parcel.writeString(isactive);
		parcel.writeString(devicetype);
		parcel.writeInt(type);
		parcel.writeString(iswol);
		parcel.writeString(isremote);
		parcel.writeString(delagentdisabled);
		parcel.writeString(isremotestop);
		parcel.writeString(subnetmask);
		parcel.writeString(powerstate);
		parcel.writeString(agentversion);
		parcel.writeString(accessauthtype);
		parcel.writeString(useaccessauth);
		parcel.writeString(groupname);
		parcel.writeBooleanArray(new boolean[] {autoSystemLock, agentInputLock, agentExecuteRestriction});
		parcel.writeInt(remoteEndAction);
		parcel.writeInt(agentUrlBlock);
		parcel.writeInt(useVideoControlMode);
		parcel.writeInt(noauthagentdelete);
	}

	public static final Creator<AgentInfo> CREATOR = new Creator<AgentInfo>() {
		public AgentInfo createFromParcel(Parcel in) {
			AgentInfo agentInfo = new AgentInfo();
			agentInfo.key = in.readString();
			agentInfo.guid = in.readString();
			agentInfo.name = in.readString();
			agentInfo.osname = in.readString();
			agentInfo.status = in.readInt();
			agentInfo.remoteip = in.readString();
			agentInfo.localip = in.readString();
			agentInfo.expired = in.readString();
			agentInfo.extend = in.readString();
			agentInfo.pflags = in.readString();
			agentInfo.rvcfgs2 = in.readString();
			agentInfo.groupid = in.readString();
			agentInfo.macaddr = in.readString();
			agentInfo.logintime = in.readString();
			agentInfo.regtime = in.readString();
			agentInfo.userid = in.readString();
			agentInfo.isactive = in.readString();
			agentInfo.devicetype = in.readString();
			agentInfo.type = in.readInt();
			agentInfo.iswol = in.readString();
			agentInfo.isremote = in.readString();
			agentInfo.delagentdisabled = in.readString();
			agentInfo.isremotestop = in.readString();
			agentInfo.subnetmask = in.readString();
			agentInfo.powerstate = in.readString();
			agentInfo.agentversion = in.readString();
			agentInfo.accessauthtype = in.readString();
			agentInfo.useaccessauth = in.readString();
			agentInfo.groupname = in.readString();

			boolean [] b = new boolean[3];
			in.readBooleanArray(b);
			agentInfo.autoSystemLock = b[0];
			agentInfo.agentInputLock = b[1];
			agentInfo.agentExecuteRestriction = b[2];

			agentInfo.remoteEndAction = in.readInt();

			agentInfo.agentUrlBlock = in.readInt();
			agentInfo.useVideoControlMode = in.readInt();
			agentInfo.noauthagentdelete = in.readInt();

			return agentInfo;
		}

		public AgentInfo[] newArray(int size) {
			return new AgentInfo[size];
		}

	};

	public int describeContents() {
		return 0;
	}

}
