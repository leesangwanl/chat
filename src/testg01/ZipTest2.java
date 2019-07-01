package testg01;

import java.io.*;
import java.util.zip.*;

public class ZipTest2 {
	public static void main(String[] args) throws IOException {
		String outputFile = "test.zip";
		int level = 9;
		FileOutputStream fout = new FileOutputStream(outputFile);
		ZipOutputStream zout = new ZipOutputStream(fout);
		zout.setLevel(level);

		ZipEntry entry = new ZipEntry("eclipse.ini");
		FileInputStream fin = new FileInputStream("eclipse.ini");
		zout.putNextEntry(entry);
		
		for (int c = fin.read(); c != -1; fin.read()) {
			zout.write(c);
		}
		
		fin.close();
		zout.close();
	}
}
