package com.jet.utils.useragent;

import org.junit.Assert;
import org.junit.Test;

import com.jet.utils.useragent.build.StevenkangMobileModelBuild;
import com.jet.utils.useragent.model.StevenkangMobileModel;

public class MobileModelParseTest {
	@Test
	public void uaaTest() {
		StevenkangMobileModelBuild mmp=new StevenkangMobileModelBuild();
		StevenkangMobileModel mm = mmp.getMobileModel("Samsung SM-C9000", "Samsung");
		System.out.println(mm);
		Assert.assertEquals("a", "a");
		
	}
}
