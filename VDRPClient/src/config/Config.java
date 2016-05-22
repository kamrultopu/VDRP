package config;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.NoSuchPaddingException;

import client.vdrp.ElGamal;

public class Config {
	public ElGamal eg;
	public Config(String filename) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException {
		
		String privateKeyFile = "Config//privateKey";
		eg = new ElGamal(privateKeyFile);
	}

	public Config() throws IOException, ClassNotFoundException {
		
		eg = new ElGamal();
		
	}

}
