package vdrp;

import config.Mode;
import client.vdrp.VdrpClient;



public class Main {
	public static void main(String[]    args) throws Exception{
		VdrpClient vd = new VdrpClient();
		String filename = "FileFolder\\Test.txt";
		System.out.println(vd.uploadFile(filename, 3));
		System.out.println(vd.getFile(filename,0,Mode.ACCESS_MODE));
    }
}
