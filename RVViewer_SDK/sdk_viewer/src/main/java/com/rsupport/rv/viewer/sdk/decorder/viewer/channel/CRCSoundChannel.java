package com.rsupport.rv.viewer.sdk.decorder.viewer.channel;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.commons.rc45.IRC45Observer;
import com.rsupport.rv.viewer.sdk.commons.rc45.RC45Stream;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.model.SoundCodecInfo;
import com.rsupport.rv.viewer.sdk.decorder.model.SoundVolInfo;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.session.SessionManager;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


public class CRCSoundChannel extends CRCChannel implements Runnable, IRC45Observer {
	public interface SoundChannelProgressListener {
		void onConnectRequest();
		void onConnect();
		void onFail();
	}
	private SoundChannelProgressListener soundChannelProgressListener = null;

	public CRCSoundChannel() {
		super(false);

		RuntimeData.getInstance().currentSessionData.soundGuid = getGuid();
	}

	public void startThread() {
		if (getMainThread() != null) {
			setThreadRun(false);
			getMainThread().interrupt();
		}

		setThreadRun(true);
		setMainThread(new Thread(this));
		getMainThread().start();
	}

	private void __stopThread() {
		setThreadRun(false);
		if (getMainThread() != null) {
			getMainThread().interrupt();
			setMainThread(null);
		}
	}
	
	public SoundCodecInfo soundCodec;
	private void sendCodecInfo(int sampleLate) {
		if(soundCodec == null){
			soundCodec = new SoundCodecInfo();
		}
		RLog.d("sendCodecInfo : " + sampleLate);
		soundCodec.setSampleRate(sampleLate);
		byte[] byteBuf = new byte[soundCodec.size()];
		soundCodec.push(byteBuf, 0);
		sendPacket(channelConstants.rcpSoundShare, channelConstants.rcpSound_CodeInfo, byteBuf, soundCodec.size());
	}
	
	private void sendVersionInfo() {
		//versionInfo 
		// int  nVersion;     // 2 = (Sampling_Multi) // RemotePc는 2로 세팅
        // char szData[32];      // 나중 확장을 위하여
		
		byte[] byteBuf = new byte[36];
		RLog.d("sendVersionInfo : Sampling_Multi");
		
		System.arraycopy(Converter.getBytesFromIntLE(2), 0, byteBuf, 0, 4);
		
		sendPacket(channelConstants.rcpSoundShare, channelConstants.rcpSound_VersionInfo, byteBuf, byteBuf.length);
	}
		

	private void sendVolumeInfo() {
		SoundVolInfo soundVolume = new SoundVolInfo();
		byte[] byteBuf = new byte[soundVolume.size()];
		soundVolume.push(byteBuf, 0);
		sendPacket(channelConstants.rcpSoundShare, channelConstants.rcpSound_CodeInfo, byteBuf, soundVolume.size());
	}

	private final static int SOUND_NORMAL 	= 0;
	private final static int SOUND_HXAUDIO	= 1;
	
	private int mSoundType = SOUND_NORMAL;

	public void run() {
		if(connectChannel(SessionManager.CHANNELNUM_SOUND, RC45Stream.BUFFER_SIZE, this, null)) {
			GlobalStatic.isShareSound = true;
			keepSessionThread(true);

			if (mSoundType == SOUND_NORMAL) {
				procRead();
			} else {
				procReadHXAudio();
			}
		}else {
			RLog.e("[Connect] Fail!");
			setThreadRun(false);
//			if (Global.GetInstance().getCurrentActivity() == Global.GetInstance().getPcViewer()) {
//				Global.GetInstance().getPcViewer().shareSoundErrorToast();
//			}

			if(soundChannelProgressListener != null) {
				soundChannelProgressListener.onFail();
			}

			GlobalStatic.isShareSound = false;
		}
	}
	
	private void procRead() {
		sendVersionInfo();
		sendCodecInfo(GlobalStatic.SCREENSET_SOUNDQUALITY);
		
		RcpPacket packet = new RcpPacket();
		rcpMsg msg = new rcpMsg();

        try {
            while (isRun()) {
                if (!readExact(packet)){
                	throw new IllegalStateException("sound channel read failure.");
				}
                switch(packet.payload) {
                    case channelConstants.rcpSoundShare:
                        // encode sound data
                        readMsgSendToOwner(packet);
                        break;
                    case channelConstants.rcpChannelNop:
                        readNop();
                        break;
                    default:
                        break;
                }
            }

        }
        catch (Exception ex) {
        	if(soundChannelProgressListener != null){
				soundChannelProgressListener.onFail();
			}

            final IChannelListener channelListener = getChannelListener();
            if(channelListener != null){
				channelListener.onException(ex);
			}
        }
	}
	
	private void procReadHXAudio() {
		RcpPacket packet = new RcpPacket();
		rcpMsg msg = new rcpMsg();
		try {
			while (isRun()) {
				if (!readExact(packet)) break;
				switch(packet.payload) {
				case channelConstants.rcpHXEngineData :
					readMsg(packet);
					break;
				case channelConstants.rcpChannelNop:
					readNop();
					break;
				default:
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 	
	}
	
	public boolean readMsg(RcpPacket packet) {
		rcpMsg	msg = new rcpMsg();
		boolean	bResult = false;

		if(packet.msgSize > 0)
		{
			byte[] buf = readExact( packet.msgSize);
			if(buf == null)
				bResult = false;
			else {
				bResult = true;
				msg.save2(buf, 0, 0, packet.msgSize);
			}
		}
		
		if(bResult) {
			ReadChannelFunc(packet.payload, msg);
		}

		return bResult;
	}

	public void ReadChannelFunc(int payloadtype, rcpMsg msg) {
		switch (payloadtype) {
		case channelConstants.rcpAVChannelReady:
//			sendVideoStart();
			break;
		case channelConstants.rcpVideoStart:
			break;
		case channelConstants.rcpAudioStart:
			break;
		case channelConstants.rcpVideoStop:
			break;
		case channelConstants.rcpAudioStop:
			break;
		case channelConstants.rcpVideoDataHeader:
//			Global.GetInstance().canvas.saveHXVideoInfo(msg.data);
			break;
		case channelConstants.rcpVideoData:
//			Global.GetInstance().canvas.setHXVideoData(msg.data);
			break;
		case channelConstants.rcpAudioHeader:
			Global.GetInstance().getScreenController().saveHXSoundInfo(msg.data);
			break;
		case channelConstants.rcpAudioData:
			//Global.GetInstance().canvas.setHXSoundData(msg.data);
			break;
		case channelConstants.rcpHXMouseEvent:
			break;
		default:
			break;
		}
	}
	
	public void readChannelDataFunc(int payload, rcpMsg msg) {
		IChannelListener channelListener = getChannelListener();

		switch (payload) {
		case channelConstants.rcpSoundShare:
			switch (msg.id) {
			case channelConstants.rcpSound_Data:
				//RLog.d("-------------- ReadChannelDataFunc : rcpSound_Data");
				recvAudio(msg.data);
				break;
			case channelConstants.rcpSound_Vol:
//				RLog.d("-------------- ReadChannelDataFunc : rcpSound_Vol");
				break;
			case channelConstants.rcpSound_ErrorInfo:
				// error on host
				RLog.d("-------------- ReadChannelDataFunc : rcpSound_ErrorInfo");

				if(channelListener != null){
					channelListener.onException(new IllegalStateException("rcpSound_ErrorInfo"));
				}

//				if(Global.GetInstance().getPcViewer() != null) {
//					Global.GetInstance().getPcViewer().shareSoundErrorToast();
//				}
				if(soundChannelProgressListener != null){
					soundChannelProgressListener.onFail();
				}
				break;
			case channelConstants.rcpSound_ErrorInfo_Format:	
//				RLog.d("-------------- ReadChannelDataFunc : rcpSound_ErrorInfo_Format");
				if(channelListener != null){
					channelListener.onException(new IllegalStateException("rcpSound_ErrorInfo_Format"));
				}

//				if(Global.GetInstance().getPcViewer() != null) {
//					Global.GetInstance().getPcViewer().shareSoundErrorToast();
//				}
				if(soundChannelProgressListener != null){
					soundChannelProgressListener.onFail();
				}
				break;
			case channelConstants.rcpSound_CodeInfo:
//				RLog.d("-------------- ReadChannelDataFunc : rcpSound_CodeInfo");
				break;
			default:
//				RLog.d("-------------- ReadChannelDataFunc : default > " + msg.id);
				break;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean releaseAll() {
		__stopThread();
		GlobalStatic.isShareSound = false;
		return super.releaseAll();
	}

	@Override
	public void onConnecting() {
		if(getChannelListener() != null) {
			getChannelListener().requestConnect(getConnectGuid(), getStreamListenPort());
		}else {
			RLog.w("SoundChannel connection requester not implemented");
		}

		if (soundChannelProgressListener != null) {
			soundChannelProgressListener.onConnectRequest();
		}
	}

	@Override
	public void onConnected() {
		if (soundChannelProgressListener != null) {
			soundChannelProgressListener.onConnect();
		}
	}
	
	public void changeSoundInfo(int val){
		RLog.d("hyusound : changeSoundInfo CRCSound : " + val);
		
		soundCodec.setSampleRate(val);
		//추후 연결시 열기
		sendCodecInfo(val);
	}

    String LOG_TAG = "CRCSoundChannel";
    static final int SAMPLE_RATE = 8000;
    static final int CHANNEL_SIZE = 1;
    AudioTrack track;
//    SpeexDecoder speexDecoder = null;

    public void recvAudio(byte[] data) {
        byte[] decoded = null;
        int bytesRead = 0;

        if (!GlobalStatic.isShareSound) return;

        try {
            if (track == null) {
                int sampleLate = 16000;
                if(soundCodec != null){
                    sampleLate = soundCodec.sampleRate;
                }
                int intSize = AudioTrack.getMinBufferSize(sampleLate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleLate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, intSize, AudioTrack.MODE_STREAM);
                track.play();

//                speexDecoder = new SpeexDecoder();
//                //init(첫번째 음역대, 8000 = 0 NB, 16000 = 1 WB, 32000 = 2 UWB)
//
//                speexDecoder.init((sampleLate/8000)==4?(2):((sampleLate/8000)-1), sampleLate, CHANNEL_SIZE, true);
            }

            if (track != null && track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
//                speexDecoder.processData(data, 0, data.length);
//                decoded = new byte[speexDecoder.getProcessedDataByteSize()];
//                bytesRead = speexDecoder.getProcessedData(decoded, 0);

                track.write(decoded, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAudio() {
        if (track != null) {
            track.stop();
            track.release();
            track = null;
        }
    }

	public void setSoundChannelProgressListener(SoundChannelProgressListener soundChannelListener) {
		this.soundChannelProgressListener = soundChannelListener;
	}
}

