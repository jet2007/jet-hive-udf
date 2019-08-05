package com.jet.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesUtils {
	

	
	/**
	 * inputStream 转化成本地文件
	 * @param inputStream 
	 * @param targetFilePath 目标文件的全路径地址
	 * @return File类型
	 */
	public static File inputStream2File(InputStream inputStream,String targetFilePath){
		try {
			
			File targetFile = new File(targetFilePath);
			if(targetFile.exists()){
				targetFile.delete();
			}
			OutputStream outStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[inputStream.available()];
			int bytesRead;
            //读取到缓冲区
            while((bytesRead = inputStream.read(buffer)) !=-1){
            	outStream.write(buffer, 0, bytesRead);
            } 
			outStream.flush();
			outStream.close();
			return targetFile;
		} catch (IOException e) {
			return null;
		} 
	}
	

}
