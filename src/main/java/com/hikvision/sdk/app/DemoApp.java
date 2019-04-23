package com.hikvision.sdk.app;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hik.mcrsdk.talk.TalkClientSDK;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Application 类</p>
 *
 * @author zhangwei59 2017/3/10 14:17
 * @version V1.0.0
 */
public class DemoApp extends Application {
    private static DemoApp ins;

    @Override
    public void onCreate() {
        super.onCreate();
        ins = this;
        MCRSDK.init();
        // 初始化RTSP
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        // 初始化语音对讲
        TalkClientSDK.initLib();
        // SDK初始化
        VMSNetSDK.init(this);

        releaseDemoVideo();
    }

    public static DemoApp getIns() {
        return ins;
    }

    /**
     * 获取登录设备mac地址
     *
     * @return Mac地址
     */
    public String getMacAddress() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();
        String mac = connectionInfo.getMacAddress();
        return mac == null ? "" : mac;
    }

    /**
     * 释放demo.mp4到手机sd卡
     */
    private void releaseDemoVideo() {
        File demoVideo = new File(FileUtils.getVideoDirPath() + "/demo.mp4");
        if (demoVideo.exists()) return;
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("demo.mp4");
            FileOutputStream outputStream = new FileOutputStream(demoVideo, false);
            byte[] buffer = new byte[1024];
            while (true) {
                int len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
