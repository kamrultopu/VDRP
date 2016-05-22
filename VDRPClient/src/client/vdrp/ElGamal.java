package client.vdrp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ElGamal {
	Cipher cipher;
	KeyPairGenerator generator;
	KeyPair          pair;
	public Key pubKey;
	public Key privKey;
	public ElGamal(String filename) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeySpecException, IOException, ClassNotFoundException{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher = Cipher.getInstance("ElGamal/None/NoPadding","BC");
		SecureRandom random = new SecureRandom();
		System.out.println(cipher.getAlgorithm());
        // create the keys
        generator = KeyPairGenerator.getInstance("ElGamal");
        generator.initialize(256, random);
		byte[] input = Files.readAllBytes(Paths.get(filename));
		byte[] decoded = Base64.getDecoder().decode(input); 
		ByteArrayInputStream bistream = new ByteArrayInputStream(decoded);
        ObjectInputStream istream = new ObjectInputStream(bistream);
        Object obj = istream.readObject();
        pubKey = ((KeyPair)obj).getPublic();
        privKey = ((KeyPair)obj).getPrivate();
	}
	public ElGamal() throws IOException, ClassNotFoundException{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
			cipher = Cipher.getInstance("ElGamal/None/NoPadding","BC");
			SecureRandom random = new SecureRandom();
			System.out.println(cipher.getAlgorithm());
	        // create the keys
	        generator = KeyPairGenerator.getInstance("ElGamal");
	        generator.initialize(256, random);
	        System.out.println(generator.getAlgorithm());
	        pair = generator.generateKeyPair();
	        
	        pubKey = pair.getPublic();
	        privKey = pair.getPrivate();
	        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
	        ObjectOutputStream ostream = new ObjectOutputStream(bstream);
	        ostream.writeObject(pair);
	        byte[] keys = bstream.toByteArray();
	        byte[] encoded = Base64.getEncoder().encode(keys);
	        String privateKey = "Config//privateKey";
	        FileOutputStream fos = new FileOutputStream(privateKey);
	        fos.write(encoded);
			fos.close();
		} catch (NoSuchAlgorithmException | NoSuchProviderException
				| NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void showParameter(){
		System.out.println(new String(pubKey.getEncoded()));
		System.out.println(new String(privKey.getEncoded()));
	}
	public byte[] encryption(byte[] input, SecureRandom random){
		try {
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
			return cipher.doFinal(input);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public byte[] decryption(byte[] cipherText){
		try {
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			return cipher.doFinal(cipherText);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
