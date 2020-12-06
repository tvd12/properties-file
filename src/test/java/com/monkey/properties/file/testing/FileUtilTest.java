package com.monkey.properties.file.testing;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.FileUtil;
import com.tvd12.test.base.BaseTest;

public class FileUtilTest extends BaseTest {

	@Test
	public void test() {
		assert FileUtil.getFileExtension("filename") == null;
		assert FileUtil.getFileExtension("filename.") == null;
		assert FileUtil.getFileExtension("filename.yaml").equals("yaml");
	}
	
	@Override
	public Class<?> getTestClass() {
		return FileUtil.class;
	}
	
}
