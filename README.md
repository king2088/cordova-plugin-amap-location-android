###插件介绍
由于某些手机的限制导致cordova-plugin-geolocation插件在android平台无法进行准确的定位，因此采用高德地图（https://lbs.amap.com/）定位SDK开发了一个高德地图的cordova插件，方便进行android端定位
###支持的平台
仅支持android端
###插件安装
cordova & phonegap cmd
cordova plugin add https://github.com/king2088/cordova-plugin-amap-location.git --variable API_KEY="46461e321733f25c"
ionic cmd
ionic cordova plugin add https://github.com/king2088/cordova-plugin-amap-location.git --variable API_KEY="46461e321733f25c"

API_KEY为高德官方申请的android key
###注意事项
1、高德官方申请key时，证书的SHA1与package包名是一定要对上，否则将无法使用此插件或者出现error code 7 之类的错误
2、查看SHA1的方法，请直接到高德官方查看相应教程https://lbs.amap.com/faq/android/android-map/86/?_t=1545192857830
3、打包时必须打release包并且签名后才能使用此插件，未签名的包使用此插件是直接报错的
