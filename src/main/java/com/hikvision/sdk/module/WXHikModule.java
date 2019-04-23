package com.hikvision.sdk.module;

import com.farwolf.weex.annotation.WeexModule;
import com.farwolf.weex.base.WXModuleBase;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.HttpConstants;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.bean.LoginData;
import com.hikvision.sdk.net.bean.RootCtrlCenter;
import com.hikvision.sdk.net.bean.SubResourceNodeBean;
import com.hikvision.sdk.net.bean.SubResourceParam;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;
import com.hikvision.sdk.utils.SDKUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WeexModule(name = "hik")
public class WXHikModule extends WXModuleBase {

    @JSMethod
    public void login(HashMap param,final JSCallback callback){

        String url=param.get("url")+"";
        String user=param.get("user")+"";
        String password=param.get("password")+"";
        String mac=param.get("mac")+"";
        String loginAddress = HttpConstants.HTTPS + url;
        final HashMap res=new HashMap();
        VMSNetSDK.getInstance().Login(loginAddress, user, password, mac, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                res.put("err",-1);
                callback.invoke(res);
            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof LoginData) {
                    String appVersion = ((LoginData) obj).getVersion();
                    SDKUtil.analystVersionInfo(appVersion);
                    res.put("err",0);
                    callback.invoke(res);
                }
            }
        });
    }

    @JSMethod
    public void logout(final JSCallback callback){
        final HashMap res=new HashMap();
        VMSNetSDK.getInstance().Logout(new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                res.put("err",-1);
                callback.invoke(res);
            }

            @Override
            public void onSuccess(Object obj) {
                res.put("err",0);
                callback.invoke(res);
            }
        });
    }

    @JSMethod
    public void getRoot(final JSCallback callback){
        VMSNetSDK.getInstance().getRootCtrlCenterInfo(1, SDKConstant.SysType.TYPE_VIDEO, 999, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                if (obj instanceof RootCtrlCenter) {
                    int parentNodeType = Integer.parseInt(((RootCtrlCenter) obj).getNodeType());
                    int parentId = ((RootCtrlCenter) obj).getId();
                    HashMap res=new HashMap();
                    res.put("parentNodeType",parentNodeType);
                    res.put("id",parentId);
                    callback.invoke(res);
//                    getSubResourceList(parentNodeType, parentId);
                }
            }
        });
    }

    @JSMethod
    public void getSubNodes(int parentNodeType, int pId,final JSCallback callback) {



        VMSNetSDK.getInstance().getSubResourceList(1, 999, SDKConstant.SysType.TYPE_VIDEO, parentNodeType, String.valueOf(pId), new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                super.onFailure();

            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                if (obj instanceof SubResourceParam) {
                    List<SubResourceNodeBean> list = ((SubResourceParam) obj).getNodeList();
                    List<HashMap> res=new ArrayList<>();
                    for(SubResourceNodeBean n:list){
                        HashMap m=new HashMap();
                        m.put("code",n.getSysCode());
                        m.put("name",n.getName());
                        m.put("type",n.getNodeType());
                        m.put("id",n.getId());
                        res.add(m);
                    }
                    callback.invoke(res);
                }
            }
        });
    }



}
