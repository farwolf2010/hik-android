package com.hikvision.sdk.component;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;

import com.farwolf.weex.annotation.WeexComponent;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.widget.CustomSurfaceView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

import java.util.HashMap;

@WeexComponent(name="hikvideo")
public class WXHikVideo extends WXComponent<CustomSurfaceView> implements SurfaceHolder.Callback{

//    private SubResourceNodeBean mCamera;

    public WXHikVideo(WXSDKInstance instance, WXVContainer parent, BasicComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
    }


    @Override
    protected CustomSurfaceView initComponentHostView(@NonNull Context context) {
        CustomSurfaceView c= new CustomSurfaceView(context);
        c.getHolder().addCallback(this);
        return c;
    }

    @Override
    protected void onHostViewInitialized(CustomSurfaceView host) {
        super.onHostViewInitialized(host);
//        mCamera = (SubResourceNodeBean) ((Activity) getInstance().getContext()).getIntent().getSerializableExtra(Constants.IntentKey.CAMERA);
    }

    @JSMethod
    public void start(final String id,final JSCallback state){


        if(state!=null){
            HashMap m=new HashMap<>();
            m.put("err",-1);
            state.invokeAndKeepAlive(m);
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                CustomSurfaceView cs= getHostView();
                VMSNetSDK.getInstance().startLiveOpt(1, id, cs, SDKConstant.LiveSDKConstant.MAIN_HIGH_STREAM, new OnVMSNetSDKBusiness() {
                    @Override
                    public void onFailure() {
                        HashMap m=new HashMap<>();
                        m.put("err",1);
                        state.invoke(m);
                    }

                    @Override
                    public void onSuccess(Object obj) {
                        HashMap m=new HashMap<>();
                        m.put("err",0);
                        state.invoke(m);
                    }
                });
                Looper.loop();
            }
        }.start();
    }


    public void stop(){
        VMSNetSDK.getInstance().stopLiveOpt(1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
