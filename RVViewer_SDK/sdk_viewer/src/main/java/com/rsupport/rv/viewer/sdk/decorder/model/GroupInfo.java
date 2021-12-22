package com.rsupport.rv.viewer.sdk.decorder.model;

public class GroupInfo {
	public String key;
	public String grpid;
	public String pgrpid;
	public String grpname;
	public String agentcount;
	public String agentonlinecount;

	public int totalGroupCount = 0;
	public int totalAgentCount = 0;
	public int sortNum = 0;

	public GroupInfo() {

	}

	public GroupInfo(GroupInfo src) {
		this.key = src.key;
		this.grpid = src.grpid;
		this.pgrpid = src.pgrpid;
		this.grpname = src.grpname;
		this.agentcount = src.agentcount;
		this.agentonlinecount = src.agentonlinecount;
	}

	public void setSortNum(int num) {
		sortNum = num;
	}

}
