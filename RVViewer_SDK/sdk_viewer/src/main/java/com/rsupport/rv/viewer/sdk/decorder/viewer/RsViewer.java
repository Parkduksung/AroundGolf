package com.rsupport.rv.viewer.sdk.decorder.viewer;


import com.rsupport.rv.viewer.sdk.decorder.model.SCAP_CLIENT_INFORMATION;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOptDeskMsg;
import com.rsupport.rv.viewer.sdk.decorder.scap.scapOption2Msg;

public class RsViewer {

    final int Hgdi = 1214735465;
    final int Hdgi = 1214539625;
    final int Hpol = 1215328108;
    final int Hnil = 1215195500;
    final int Hddi = 1214538857;

    public void SetOptionHelper(SCAP_CLIENT_INFORMATION cli_opt, scapOption2Msg opts) // cli_opt <- opts
    {
        cli_opt.desk.rcSrn = opts.hook.rcSrn;
        switch (opts.hook.scapHookType) {
            case scapOptDeskMsg.scapHookGdi:
                cli_opt.desk.hookType = Hgdi;
                break;
            case scapOptDeskMsg.scapHookDdi:
                cli_opt.desk.hookType = Hddi;
                break;
            case scapOptDeskMsg.scapHookDgi:
                cli_opt.desk.hookType = Hdgi;
                break;
            case scapOptDeskMsg.scapHookPol:
                cli_opt.desk.hookType = Hpol;
                break;
            default:
                cli_opt.desk.hookType = Hnil;
                break;
        }

        cli_opt.desk.hookMonitor = opts.hook.scapHookMonitor;

        cli_opt.desk.deskBitsPerPixel = opts.hook.scapLocalPxlCnt;

        cli_opt.desk.hookTimer = opts.hook.scapTriggingTime;
        cli_opt.desk.deskRotate = opts.hook.scapRotate;

        cli_opt.encoder.flags = opts.encoder.flags;
        cli_opt.encoder.encType = opts.encoder.scapEncoderType;

        cli_opt.encoder.encHostBitsPerPixel = opts.encoder.HostPxlCnt;        // Host -> Viewer.
        cli_opt.encoder.encViewerBitsPerPixel = opts.encoder.scapRemoteBpp;        // 1, 4, 8, 15/16, 24, 32 : Viewer <-> Host
        cli_opt.encoder.encJpgLowQuality = opts.encoder.scapEncJpgLowQuality;    // compression level. %
        cli_opt.encoder.encJpgHighQuality = opts.encoder.scapEncJpgHighQuality;    // compression level. %
        cli_opt.encoder.encSendTimesWithoutAck = opts.encoder.scapEncSendTimesNoAck;
        cli_opt.encoder.encMaxTileCache = (int) opts.encoder.scapTileCacheCount;
        cli_opt.encoder.encClientChannel = null;

        cli_opt.encoder.stretch.ratioWidth.cx = opts.encoder.scapXRatio.numerator;
        cli_opt.encoder.stretch.ratioWidth.cy = opts.encoder.scapXRatio.denominator;
        cli_opt.encoder.stretch.ratioHeight.cx = opts.encoder.scapYRatio.numerator;
        cli_opt.encoder.stretch.ratioHeight.cy = opts.encoder.scapYRatio.denominator;
        cli_opt.encoder.stretch.fixedWidth = opts.hook.xFixedWidth;
        cli_opt.encoder.stretch.fixedHeight = opts.hook.yFixedHeight;
    }
}
