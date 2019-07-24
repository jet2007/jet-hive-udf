package com.jet.hive.udf.ipgeo;

public class ConstantsGeoIp {

	public static final String GEOLITE2_CITY_FILE= "GeoLite2-City.mmdb";
	
	public static final String SEP= "\\|";//分隔
	public static final String SEP_IP2REGION= "\\|";//分隔
	
	public static final String FILE_IP2REGION= "ip2region.db";//分隔
	
	public static final String KEY_COUNTRY_ID= "countryID"; //国家
	public static final String KEY_COUNTRY_NAME= "countryName";
	public static final String KEY_COUNTRY_NAME_EN= "countryNameEn";
	
	public static final String KEY_PROVINCE_ID= "provinceID"; //省份
	public static final String KEY_PROVINCE_NAME= "provinceName";
	public static final String KEY_PROVINCE_NAME_EN= "provinceNameEn";
	
	public static final String KEY_CITY_ID= "cityID"; //城市
	public static final String KEY_CITY_NAME= "cityName";
	public static final String KEY_CITY_NAME_EN= "cityNameEn";
	
	public static final String KEY_ISP_ID= "ispID";  //运营商，如电信
	public static final String KEY_ISP_NAME= "ispName";
	public static final String KEY_ISP_NAME_EN= "ispNameEn";
	
	public static final String KEY_REGION_ID= "regionID";  //华南
	public static final String KEY_REGION_NAME= "regionName";
	public static final String KEY_REGION_NAME_EN= "regionNameEn";
	
	public static final String KEY_CONTINENT_ID= "continentID"; //大洲
	public static final String KEY_CONTINENT_NAME= "continentName";
	public static final String KEY_CONTINENT_NAME_EN= "continentNameEn";
	
	public static final String KEY_LATITUDE= "latitude"; //经度纬度
	public static final String KEY_LONGITUDE= "longitude";
	
}
