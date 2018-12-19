package com.amaplocation.amap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;

/**
 * This class echoes a string called from JavaScript.
 */
public class AmapLocation extends CordovaPlugin{
    // 声明AMapLocationClient类对象
    public AMapLocationClient locationClient = null;
    // 声明定位参数
    public AMapLocationClientOption locationOption = null;
    //JS 回调接口对象
    public static CallbackContext callback = null;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getCurrentPosition")) {
            callback = callbackContext;
            PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            callback.sendPluginResult(pluginResult);
            this.getCurrentPosition();
            return true;
        }
        return false;
    }

    private void getCurrentPosition() {
        if (locationClient == null) {
            this.initLocation();
        }
        this.startLocation();
    }

    // 初始化高德定位
    private void initLocation() {
        // 初始化client
        locationClient = new AMapLocationClient(this.cordova.getActivity().getApplicationContext());// this.webView.getContext()
        locationOption = getDefaultOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     * 
     * @since 2.8.0
     * @author Tony.Tao
     *
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);// 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);// 可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);// 可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);// 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);// 可选， 设
                                                                                // 网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);// 可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); // 可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); // 可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);// 可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
	 * 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation location) {
            try{
                JSONObject json = new JSONObject();
                if (null != location) {
                    if(location.getErrorCode() == 0){
                        json.put("status", "success");
                        //定位类型
                        json.put("type", location.getLocationType());
                        //经度
                        json.put("longitude", location.getLongitude());
                        //纬度
                        json.put("latitude", location.getLatitude());
                        //精度
                        json.put("accuracy", location.getAccuracy());
                        //提供者
                        json.put("provider", location.getProvider());
                        //国家
                        json.put("country", location.getCountry());
                        //省
                        json.put("province", location.getProvince());
                        //市
                        json.put("city", location.getCity());
                        //城市编码
                        json.put("citycode", location.getCityCode());
                        //区
                        json.put("district", location.getDistrict());
                        //区域码
                        json.put("adcode", location.getAdCode());
                        //地址
                        json.put("address", location.getAddress());
                        //回调时间
                        json.put("callbacktime", System.currentTimeMillis());
                    }else {
                        //定位失败
                        json.put("status", "error");
                        json.put("errCode:", location.getErrorCode());
                        json.put("errMessage:", location.getErrorInfo());
                        json.put("errDescription:", location.getLocationDetail());
                    }

                } else {
                    json.put("error", "get position is err");
                }
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, json);
                pluginResult.setKeepCallback(true);
                callback.sendPluginResult(pluginResult);
            }catch (JSONException e){
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                pluginResult.setKeepCallback(true);
                callback.sendPluginResult(pluginResult);
            }finally{
                locationClient.stopLocation();
            }
		}
    };
    
    /**
	 * 开始定位
	 * 
	 * @since 2.8.0
	 * @author Tony.Tao
	 *
	 */
	private void startLocation(){
		//根据控件的选择，重新设置定位参数
		// resetOption();
		// 设置定位参数
		// locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
    }
    
    /**
	 * 停止定位
	 * 
	 * @since 2.8.0
	 * @author Tony.Tao
	 *
	 */
	private void stopLocation(){
		// 停止定位
		locationClient.stopLocation();
    }
    
    /**
	 * 销毁定位
	 * 
	 * @since 2.8.0
	 * @author Tony.Tao
	 *
	 */
	private void destroyLocation(){
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
    }
}