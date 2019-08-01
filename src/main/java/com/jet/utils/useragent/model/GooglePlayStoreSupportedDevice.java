package com.jet.utils.useragent.model;

import com.jet.utils.string.HexUtils;


/**
 * 原始文件来源：http://storage.googleapis.com/play_public/supported_devices.csv
 * @author JET
 *
 */
public class GooglePlayStoreSupportedDevice {
	
	private String retailBranding;
	private String marketingName;
	private String device;
	private String model;
	
	private static final String FIELD_SPLIT_BY= ",";
	
	public GooglePlayStoreSupportedDevice() {
	}
	
	public GooglePlayStoreSupportedDevice(String line) {
		String[] results = line.split(FIELD_SPLIT_BY);
		this.retailBranding= HexUtils.mixHex2Str( results[0]);
		this.marketingName= HexUtils.mixHex2Str( results[1]);
		this.device= HexUtils.mixHex2Str( results[2]);
		this.model= HexUtils.mixHex2Str( results[3]);
	}

	public GooglePlayStoreSupportedDevice(String[] lineArray) {
		for (int i = 0; i < lineArray.length; i++) {
			lineArray[i]=HexUtils.mixHex2Str( lineArray[i]);
		}
		this.retailBranding= lineArray[0];
		this.marketingName=lineArray[1];
		this.device=lineArray[2];
		this.model=lineArray[3];
	}
	
	public String getRetailBranding() {
		return retailBranding;
	}

	public void setRetailBranding(String retailBranding) {
		this.retailBranding = retailBranding;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	
}
