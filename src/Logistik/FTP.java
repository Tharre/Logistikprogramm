package Logistik;

import java.io.*;

public class FTP {
	public void FTP() {
	}

	public static String temp() {
		return System.getProperty("java.io.tmpdir");
	}

	public static void upload(File f, String path) throws Exception {
		File ftpfile = new File(temp() + "upload.ftp");
		BufferedWriter bw = new BufferedWriter(new FileWriter(ftpfile));
		bw.write("open logistik.htl-hl.ac.at");
		bw.newLine();
		bw.write("logistikpdf");
		bw.newLine();
		bw.write("pdf");
		bw.newLine();
		bw.write("binary");
		bw.newLine();
		bw.write("cd pdf");
		bw.newLine();
		bw.write("cd " + path);
		bw.newLine();
		bw.write("put " + f.getAbsolutePath());
		bw.newLine();
		bw.write("bye");
		bw.newLine();
		// bw.write("del "+temp()+"upload.ftp");
		// bw.newLine();
		bw.write("quit");
		bw.close();
		String cmd = "cmd /q start cmd /c ftp -s:" + temp() + "upload.ftp";
		Runtime.getRuntime().exec(cmd);
		// cmd = "cmd /c start cmd /c del "+temp()+"upload.ftp";
		// Runtime.getRuntime().exec(cmd);
	}
}