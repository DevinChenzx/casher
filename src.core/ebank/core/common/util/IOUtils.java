/*
 * Created on 2004-12-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ebank.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IOUtils {
	public static void writeFile(String fileName,byte[] data)
	{
		try{
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(data);
			fos.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}		
	}
	
	public static ByteArrayOutputStream readFile(String filename)
	{
		try{
			FileInputStream fileInStream = new FileInputStream(filename);
			ByteArrayOutputStream fileByteStream = new ByteArrayOutputStream();
			int i = 0;
			while((i = fileInStream.read()) !=-1)
			{
				fileByteStream.write(i);
			}
			fileInStream.close();
			return fileByteStream;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
}
