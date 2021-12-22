package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;



import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer;
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.session.SessionManager;

import java.io.IOException;


public class CRCVSessionChannel extends CRCChannel implements Runnable, IRC45Observer {
	public interface ISessionChannelListener extends IChannelListener {
		void onReconnectRequested();
	}

	private ISessionChannelListener sessionChannelListener;

	public CRCVSessionChannel(boolean useServerRec) {
		super(useServerRec);
	}

	public void startThread() {
		if (getMainThread() != null) {
			setThreadRun(false);
			getMainThread().interrupt();
		}

		setThreadRun(true);
		setMainThread(new Thread(this));
		getMainThread().start();
		setSendTime(System.currentTimeMillis());
	}

	private void stopThread() {
		setThreadRun(false);
		if (getMainThread() != null) {
			getMainThread().interrupt();
			setMainThread(null);
		}
	}

	public void setListener(ISessionChannelListener listener) {
		super.setListener(listener);
		this.sessionChannelListener = listener;
	}

	private void procRead() {
		try {
			RcpPacket packet = new RcpPacket();

			while (isRun()) {
				RLog.i("Session Channel .... looping ... ");
				if (!readExact(packet)) {
					throw new IOException("Session channel read failed");
				}

				switch( (packet.payload)) {
					case channelConstants.rcpChannel:
					case channelConstants.rcpReConnect:
						readMsgSendToOwner(packet);
						break;

					case channelConstants.rcpChannelNop:
						readNop();
						break;

					default:
					RLog.i("[SECH] Unhandled payload : " + packet.payload);
				}
			}
		} catch (Exception ex) {
			RLog.i("Session Channel .... error error ... ");
			if(getChannelListener() != null) {
				getChannelListener().onException(ex);
			}
		}
	}

	public void run() {
		if(connectChannel(SessionManager.CHANNELNUM_SESSION, RC45Stream.BUFFER_SIZE, this, null)) {
			keepSessionThread();

			procRead();
		}
	}

	@Override
	public void readChannelDataFunc(int payload, rcpMsg msg) {
		switch(payload) {
			case channelConstants.rcpChannel: {
				RLog.d("rcpChannel message : " + msg.msgID);
				break;
			}

			case channelConstants.rcpReConnect:
				handleReconnectPayload(msg);
				break;

			default:
				RLog.i("Unprocessed session channel packet: " + payload + " / " + msg.msgID);
				break;
		}
	}

	private void handleReconnectPayload(rcpMsg msg) {
		switch(msg.msgID) {
			case channelConstants.rcpReConnectRequest: {
				RLog.d("Reconnect requested");
				sessionChannelListener.onReconnectRequested();
				break;
			}

			default:
				RLog.i("Unprocessed reconnect packet message: " + msg.msgID);
				break;
		}
	}

	@Override
	public boolean releaseAll() {
		stopThread();
		return super.releaseAll();
	}

	@Override
	public void onConnecting() {
		if (getChannelListener() != null) {
			getChannelListener().requestConnect(getConnectGuid(), getStreamListenPort());
		}
	}

	@Override
	public void onConnected() {
	}

}


