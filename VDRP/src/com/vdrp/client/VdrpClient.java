package com.vdrp.client;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import com.vdrp.config.Config;
import com.vdrp.config.Mode;





public class VdrpClient {
	static final String UPLOAD_URL = "http://localhost:8080//VDRPServer//Uploader";
	static final String DOWNLOAD_URL="http://localhost:8080//VDRPServer//Access";
	static final int BUFFER_SIZE = 32;

	Config cfg;
	final int BYTE_SIZE = 32;
	public VdrpClient() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException{
		cfg = getConfig();
		//eg.showParameter();
	}
	private Config getConfig() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException{
		Config cfg;
		String configFileName = "Config//privateKey";
		File conf = new File(configFileName);
		if( conf.exists()){
			cfg = new Config(configFileName);
		} else {
			cfg = new Config();
		}
		return cfg;
	}

	public String uploadFile(String filename, int nCopy) throws IOException{
		String retVal = null;
		System.out.println("in uploader API");
		for( int i = 0 ; i < nCopy ; i++){
			retVal = uploadToServer(filename,i);
		}
		return retVal;
	}
	public String uploadToServer(String filename, int nCopy) throws IOException{
		File ifile = new File(filename);
		URL url = new URL(UPLOAD_URL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		// sets file name as a HTTP header
		httpConn.setRequestProperty("fileName", ifile.getName());
		httpConn.setRequestProperty("copy", Integer.toString(nCopy));
		// opens output stream of the HTTP connection for writing data
        OutputStream outputStream = httpConn.getOutputStream();
        //System.out.println(httpConn.getResponseCode());
        // Opens input stream of the file for reading data
        FileInputStream inputStream = new FileInputStream(filename);

        byte[] buffer = new byte[BUFFER_SIZE];


        System.out.println("Start writing data...");
        FileInputStream is = new FileInputStream(ifile);

        int bytesRead;
		SecureRandom rnd = new SecureRandom();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
        	byte[] cipherText = cfg.eg.encryption(buffer, rnd);
            outputStream.write(cipherText, 0, cipherText.length);
        }

        outputStream.close();
        inputStream.close();

        // always check HTTP response code from server
        int responseCode = httpConn.getResponseCode();
        System.out.println(responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reads server's response
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String response = reader.readLine();
            System.out.println("Data was written.");
            return response;
        } else {
            return "ERROR:"+responseCode;
        }
	}
	public int getFile(String folderToSave, String filename, int copyNumber, Mode md) throws IOException{
		File ifile = new File(filename);
		URL url = new URL(DOWNLOAD_URL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestProperty("fileName", ifile.getName());
		httpConn.setRequestProperty("copy", Integer.toString(copyNumber));
		int responseCode = httpConn.getResponseCode();
		System.out.println("starting");

		if(responseCode == HttpURLConnection.HTTP_OK){
			System.out.println(responseCode);
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();
			if( disposition != null ){
				int index = disposition.indexOf("filename=");
				if(index>0){
					filename = disposition.substring(index+9, disposition.length());
				}
			}
			if( md == Mode.ACCESS_MODE){
				int index = filename.indexOf(".txt");
		        String ext = filename.substring(index);
		        filename = filename.substring(0, index-1) + ext;
			}

			InputStream inputStream = httpConn.getInputStream();
			File downloadFile = new File(folderToSave+"\\"+filename);
			FileOutputStream outputStream = new FileOutputStream(downloadFile);
			System.out.println(inputStream.available());
			byte[] buffer = new byte[2*BUFFER_SIZE];
			int bytesRead = -1;
			//FileOutputStream os = new FileOutputStream(new File("checktest.txt"));

			while((bytesRead=inputStream.read(buffer)) != -1 ){
				if( md == Mode.ACCESS_MODE){
					byte[] plainText = cfg.eg.decryption(buffer);
					outputStream.write(plainText, 0, plainText.length);
				} else {
					outputStream.write(buffer, 0, buffer.length);
				}
			}

			//System.out.println(bytesRead);
            outputStream.close();
            inputStream.close();
            //os.close();
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();

		return responseCode;

	}
	public int verifyRedundancy(String filename, int n) throws IOException, NoSuchAlgorithmException{
		int verifiedRedundancy = 0 ;
		File file = new File("Temp");

		if( !file.exists()){
			file.mkdir();
		}
		getFile(file.getAbsolutePath(),filename,0,Mode.ACCESS_MODE);
		for( int i = 0 ; i < n ; i++){
			int response = getFile(file.getAbsolutePath(),filename,i,Mode.VERIFICATION_MODE);
			if(response == 200 && checkIntegrity(file,filename,i)){
				verifiedRedundancy++;
			}
		}
		System.out.println(file.delete());;
		return verifiedRedundancy;
	}
	private boolean checkIntegrity(File file, String filename, int targetIndex) throws NoSuchAlgorithmException, IOException {
		int index = filename.indexOf(".txt");
        String ext = filename.substring(index);
        String rootfilename = file.getAbsolutePath()+"\\"+filename;
        String checkFilename = file.getAbsolutePath()+"\\"+filename.substring(0, index)+ Integer.toString(targetIndex) + ext;
        Path path = Paths.get(filename);
		File ifile = new File(checkFilename);
		File ofile = new File(file.getAbsolutePath()+"\\"+"check"+ext);
		try {
			ofile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] chunk = new byte[2*BYTE_SIZE];
		try {
			FileInputStream is = new FileInputStream(ifile);
			FileOutputStream os = new FileOutputStream(ofile);
			int chunkLen = 0;
			int i = 0 ;
			while((chunkLen = is.read(chunk)) != -1 ){
				byte[] plainText = cfg.eg.decryption(chunk);
				os.write(plainText, 0, chunkLen/2);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        //File source = new File(rootfilename);
        //File chekFile = new File(checkFilename);
        MessageDigest sourceDg = MessageDigest.getInstance("MD5");
        sourceDg.update(Files.readAllBytes(Paths.get(rootfilename)));
        byte[] source = sourceDg.digest();
        sourceDg.update(Files.readAllBytes(Paths.get(checkFilename)));
        byte[] checkEnc = sourceDg.digest();
        sourceDg.update(Files.readAllBytes(Paths.get(ofile.getAbsolutePath())));
        byte[] check = sourceDg.digest();
        boolean response = false;
		if( Arrays.equals(source, check) ){
			for( int i = 0 ; i < targetIndex ; i++){
				String tempFilename = file.getAbsolutePath()+"\\"+filename.substring(0, index)+ Integer.toString(i) + ext;
				sourceDg.update(Files.readAllBytes(Paths.get(tempFilename)));
				byte[] temp = sourceDg.digest();
				if( Arrays.equals(checkEnc, temp)){
					return false;
				}
			}
			return true;
		}
		return response;
	}
}



/*File ifile = new File(filename);
Path path = Paths.get(filename);
int index = path.getNameCount();
File[] oFile = new File[nCopy];
for( int i = 0 ; i < nCopy ; i++){
	oFile[i] = new File("UploadFolder\\"+path.getFileName() + i);
	try {
		oFile[i].createNewFile();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}

byte[] chunk = new byte[BYTE_SIZE];
try {
	for( int i = 0 ; i < nCopy ; i++){
		FileInputStream is = new FileInputStream(ifile);
		FileOutputStream os = new FileOutputStream(oFile[i]);
		int chunkLen = 0;
		SecureRandom rnd = new SecureRandom();
		while((chunkLen = is.read(chunk)) != -1 ){
			byte[] cipherText = eg.encryption(chunk, rnd);
			os.write(cipherText);
		}
		is.close();
		os.close();
	}
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

		Path path = Paths.get(filename);
		File ifile = new File("UploadFolder\\"+path.getFileName()+2);
		File ofile = new File(path.getFileName().toString());
		try {
			ofile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] chunk = new byte[2*BYTE_SIZE];
		try {
			FileInputStream is = new FileInputStream(ifile);
			FileOutputStream os = new FileOutputStream(ofile);
			int chunkLen = 0;
			int i = 0 ;
			while((chunkLen = is.read(chunk)) != -1 ){
				byte[] plainText = eg.decryption(chunk);
				os.write(plainText, 0, chunkLen/2);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
*
*/
