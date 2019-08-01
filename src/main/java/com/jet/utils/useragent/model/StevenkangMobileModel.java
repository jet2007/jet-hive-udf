package com.jet.utils.useragent.model;

import com.jet.utils.useragent.constant.ConstantsUserAgent;

/**
 * 原始文件来源：https://github.com/stevenkang/model-lib mobile.txt
 * 加工逻辑：doc/mobile-model.xlsx(由4列变7列)
 * 数据源质量不高，推荐使用隔壁的
 * @author JET
 *
 */
public class StevenkangMobileModel {
	private String model;
	private String modelName;
	private String brand;
	private String manufacturerCn;
	private String manufacturerEn;
	private String brandCn;
	private String brandEn;
	
	public StevenkangMobileModel() {
	}
	
	public StevenkangMobileModel(String line) {
		String[] results = line.split(ConstantsUserAgent.STEVENKANG_RESOURCE_FILE_SPLIT_BY);
		this.model=results[0];
		this.modelName=results[1];
		this.brand=results[2];
		this.manufacturerCn=results[3];
		this.manufacturerEn=results[4];
		this.brandCn=results[5];
		this.brandEn=results[6];
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String name) {
		this.modelName = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brandOld) {
		this.brand = brandOld;
	}

	public String getManufacturerCn() {
		return manufacturerCn;
	}

	public void setManufacturerCn(String manufacturerCn) {
		this.manufacturerCn = manufacturerCn;
	}

	public String getManufacturerEn() {
		return manufacturerEn;
	}

	public void setManufacturerEn(String manufacturerEn) {
		this.manufacturerEn = manufacturerEn;
	}

	public String getBrandCn() {
		return brandCn;
	}

	public void setBrandCn(String brandCn) {
		this.brandCn = brandCn;
	}

	public String getBrandEn() {
		return brandEn;
	}

	public void setBrandEn(String brandEn) {
		this.brandEn = brandEn;
	}
	
	@Override
	public String toString() {
		String s="manufacturerCn="+this.manufacturerCn +
				",manufacturerEn="+this.manufacturerEn +
				",brandCn="+this.brandCn +
				",brandEn="+this.brandEn +
				",brand="+this.brand +
				",brand="+this.model +
				",name="+this.modelName ;
		return s;
	}

}
