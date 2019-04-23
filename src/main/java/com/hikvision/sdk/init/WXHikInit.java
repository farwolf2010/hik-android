package com.hikvision.sdk.init;

import android.app.Application;
import android.content.Context;

import com.farwolf.weex.annotation.ModuleEntry;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hik.mcrsdk.talk.TalkClientSDK;
import com.hikvision.sdk.VMSNetSDK;

@ModuleEntry
public class WXHikInit {


    public void init(Context c){
        MCRSDK.init();
        // 初始化RTSP
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        // 初始化语音对讲
        TalkClientSDK.initLib();
        // SDK初始化
        VMSNetSDK.init((Application)c);
    }
}
