package com.example.mystep.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.mystep.callback.mModifyCallback;
import com.example.mystep.model.LoginModel;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class utils {
    public static final String getValidateCode = "https://sports.lifesense.com/sms_service/verify/getValidateCode?requestId=1000&sessionId=nosession&mobile=";
    public static final String sendCodeWithOptionalValidate = "https://sports.lifesense.com/sms_service/verify/sendCodeWithOptionalValidate?sessionId=nosession&requestId=1000";

    public static final String uploadMobileStep ="https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2" +
            "?city=%E4%B8%8A%E6%B5%B7&province=%E4%B8%8A%E6%B5%B7%E5%B8%82&devicemodel=Magic2&areaCode=310109&osversion=5.1.1" +
            "&screenHeight=1280&provinceCode=310000&version=4.5&channel=huawei&systemType=2&promotion_channel=huawei&screenWidth=720" +
            "&requestId=d6e3e55379914cbd86ebbe975b19a877&longitude=121.492479&screenheight=1280&os_country=CN" +
            "&timezone=Asia%2FShanghai&cityCode=310100&os_langs=zh" +
            "&platform=android" +
            "&clientId=8e844e28db7245eb81823132464835eb" +
            "&openudid=&countryCode=&country=%E4%B8%AD%E5%9B%BD&screenwidth=720" +
            "&network_type=wifi&appType=6&area=CN&latitude=31.247221&language=zh";

    public static final String loginByAuth="https://sports.lifesense.com/sessions_service/loginByAuth" +
            "?city=%E4%B8%8A%E6%B5%B7&province=%E4%B8%8A%E6%B5%B7%E5%B8%82&devicemodel=Magic2" +
            "&areaCode=310109&osversion=5.1.1&screenHeight=1280&provinceCode=310000&version=4.5&channel=huawei" +
            "&systemType=2&promotion_channel=huawei&screenWidth=720&requestId=d6e3e55379914cbd86ebbe975b19a877" +
            "&longitude=121.492479&screenheight=1280&os_country=CN&timezone=Asia%2FShanghai&cityCode=310100" +
            "&os_langs=zh&platform=android&clientId=8e844e28db7245eb81823132464835eb&openudid=&countryCode=" +
            "&country=%E4%B8%AD%E5%9B%BD&screenwidth=720&network_type=wifi&appType=6&area=CN&latitude=31.247221&language=zh";
    /*动态权限获取*/
    public static void verifyStoragePermissions(Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_PRIVILEGED_PHONE_STATE"
        };
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void modifyByCookie(String steps, String userid,String cookies, Activity activity) throws ParseException {
        if (Integer.parseInt(steps)>=20000){
            steps = "20000";
        }
        LoginModel loginModel = new LoginModel();
        loginModel.postAsynModifyStepsHttpByCookie(activity,uploadMobileStep, steps,userid,cookies, new mModifyCallback() {
            @Override
            public void onModifySucess(String msg) {
                Looper.prepare();
                XToastUtils.success(msg);
                Looper.loop();
                Log.e("", "这是修改成功的回调msg="+msg);
            }

            @Override
            public void onModifyFailed() {
                Looper.prepare();
                XToastUtils.error("未知");
                Looper.loop();
                Log.e("", "这是修改失败的回调msg=");
            }
        });
    }

    /**
     * 获取设备UUID
     */
    public static String getMyUUID(Activity activity) {
        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();//设备唯一号码
        tmSerial = "" + tm.getSimSerialNumber();//sim 卡标识
        androidId = ""
                + android.provider.Settings.Secure.getString(
                activity.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);//在设备首次启动时，系统会随机生成一个64位的数字
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());


        return deviceUuid.toString();
    }
    public static String getMacAddress() {
        List<NetworkInterface> interfaces = null;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                if (networkInterface != null && TextUtils.isEmpty(networkInterface.getName()) == false) {
                    if ("wlan0".equalsIgnoreCase(networkInterface.getName())) {
                        byte[] macBytes = networkInterface.getHardwareAddress();
                        if (macBytes != null && macBytes.length > 0) {
                            StringBuilder str = new StringBuilder();
                            for (byte b : macBytes) {
                                str.append(String.format("%02X:", b));
                            }
                            if (str.length() > 0) {
                                str.deleteCharAt(str.length() - 1);
                            }
                            return str.toString();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

}
