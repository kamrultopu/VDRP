package client.vdrp;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ElGamal {
	Cipher cipher;
	KeyPairGenerator generator;
	KeyPair          pair;
	Key pubKey;
	Key privKey;
	public ElGamal(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
			cipher = Cipher.getInstance("ElGamal/None/NoPadding","BC");
			SecureRandom random = new SecureRandom();
	        // create the keys
	        generator = KeyPairGenerator.getInstance("ElGamal");
	        generator.initialize(256, random);

	        pair = generator.generateKeyPair();
	        pubKey = pair.getPublic();
	        privKey = pair.getPrivate();
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
