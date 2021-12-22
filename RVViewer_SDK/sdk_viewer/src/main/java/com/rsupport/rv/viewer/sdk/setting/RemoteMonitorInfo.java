/**
 * @author kyeom
 * @date   2009.08.18
 *
 * 원격 모니터 정보
 */

package com.rsupport.rv.viewer.sdk.setting;


import com.rsupport.rv.viewer.sdk.decorder.model.RECT;

public class RemoteMonitorInfo {

    public RECT totalRect = null;
    public int totalCount = 0;
    public int[] monitorNum = null;
    public RECT[] monitorRect = null;
    public int[] monitorColor = null;
    private int currentMonitor = -1;

    public RECT getTotalRect() {
        return totalRect;
    }
    public void setTotalRect(RECT totalRect) {
        this.totalRect = totalRect;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int monitorCount) {
        this.totalCount = monitorCount;
    }
    public int[] getMonitorNum() {
        return monitorNum;
    }
    public void setMonitorNum(int[] monitorNum) {
        this.monitorNum = monitorNum;
    }
    public RECT[] getMonitorRect() {
        return monitorRect;
    }
    public void setMonitorRect(RECT[] monitorRect) {
        this.monitorRect = monitorRect;

        if(currentMonitor == -1) {
            currentMonitor = 0;
        }
    }

    public void setCurrentMonitorNumber(int number) {
        //number == 1 based
        currentMonitor = Math.min(Math.max(0, number-1), ((monitorRect != null) ? monitorRect.length-1 : -1));
    }

    public int getCurrentMonitorNumber() {
        return currentMonitor;
    }

    public int[] getMonitorColor() {
        return monitorColor;
    }
    public void setMonitorColor(int[] monitorColor) {
        this.monitorColor = monitorColor;
    }

    public RECT getCurrentRect() {
        return monitorRect[currentMonitor];
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Remote monitors: \n");
        for(RECT r : monitorRect) {
            sb.append(String.format("Monitor RECT: (%d x %d) ((%d, %d) ~ (%d, %d))\n", r.getWidth(), r.getHeight(), r.left, r.top, r.right, r.bottom));
        }

        return sb.toString();

    }
}
